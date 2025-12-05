package org.lds.mobile.navigation3.navigator

import androidx.annotation.VisibleForTesting
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/**
 * A thread-safe store for managing navigation results and their lifecycle.
 *
 * [ResultStore] provides a centralized mechanism for storing and retrieving results that can be
 * shared between navigation destinations. It supports:
 * - Setting and retrieving results with type-safe operations
 * - Removing results with optional callbacks
 * - Observing result changes via coroutine flows
 * - Registering listeners for result updates
 *
 * ## Thread Safety
 * All operations are thread-safe through the use of reentrant locks. The store maintains an internal
 * lock ([reentrantLock]) that protects access to the result map and callback registrations.
 *
 * ## Callback Execution
 * Callbacks are invoked **outside** of the lock to prevent potential deadlocks when callbacks attempt
 * to acquire the lock themselves. This design allows callbacks to safely perform operations that
 * might require synchronization.
 *
 * ## Memory Management
 * Results remain in the store indefinitely until explicitly removed via [removeResult], or by setting
 * a result to null via [setResult]. Flows do not automatically clean up results when collectors stop collecting.
 *
 * ## Usage Patterns
 *
 * **Setting and retrieving results:**
 * ```
 * ResultStore.setResult(MyResultKey, "some value")
 * val value: String? = ResultStore.getResult<String>(MyResultKey)
 * ```
 *
 * **Observing results as a Flow:**
 * ```
 * ResultStore.getResultFlow<String>(MyResultKey).collect { result ->
 *     // Handle result changes, including null when removed
 *     if (result != null) ResultStore.removeResult<String>(MyResultKey)
 * }
 * ```
 *
 * **Atomic get-and-remove operations:**
 * ```
 * val value: String? = ResultStore.getAndRemoveResult<String>(MyResultKey)
 * ```
 */
object ResultStore {
    private val lock = reentrantLock()
    private val resultMap = mutableMapOf<ResultKey, Any>()
    private val resultCallbacks = mutableMapOf<ResultKey, MutableList<ResultStoreCallback>>()

    /**
     * Retrieves an untyped result value from the store.
     *
     * This is a lower-level method that returns the result without type checking. In most cases,
     * [getResult] should be preferred for type-safe access. This method is primarily used internally
     * by [getResult] and is marked with [VisibleForTesting] to support unit testing.
     *
     * This method is thread-safe and uses internal locking to ensure consistent access to the
     * result map.
     *
     * @param key The [ResultKey] identifying the result
     * @return The stored result as Any, or null if no result is stored for the given key
     *
     * @see getResult for type-safe result retrieval
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getResultValue(key: ResultKey): Any? = lock.withLock {
        resultMap[key]
    }

    /**
     * Retrieves a type-safe result value from the store.
     *
     * This is the primary method for retrieving stored results. The result is cast to the reified
     * type [T] using a safe cast operator. If the stored value is not compatible with the requested
     * type, null is returned rather than throwing an exception.
     *
     * The type parameter [T] is reified, meaning the actual type is available at runtime, allowing
     * safe type checking. This method is thread-safe and uses internal locking to ensure consistent
     * access to the result map.
     *
     * @param T The expected type of the stored result
     * @param key The [ResultKey] identifying the result to retrieve
     * @return The stored result cast to type [T], or null if no result exists for the key or
     *         if a type mismatch occurs (e.g., stored value is a String but [T] is Int)
     *
     * @see removeResult for type-safe removal
     * @see setResult to store a result
     * @see getResultFlow for observing result changes via Flow
     *
     * Example:
     * ```
     * ResultStore.setResult(MyKey, "Hello World")
     * val text: String? = ResultStore.getResult<String>(MyKey) // Returns "Hello World"
     * val number: Int? = ResultStore.getResult<Int>(MyKey)    // Returns null (type mismatch)
     * ```
     */
    inline fun <reified T : Any> getResult(key: ResultKey): T? {
        return getResultValue(key) as T?
    }

    /**
     * Stores a result in the store and notifies all registered listeners.
     *
     * If a result with the same key already exists, it will be overwritten. Passing null
     * as the value removes the result from the store. All registered callbacks are invoked
     * with the new value after the operation completes.
     *
     * This method is thread-safe and uses internal locking to ensure that the result map and
     * callback list are accessed consistently. Callbacks are invoked outside the lock to allow
     * them to safely perform their own operations.
     *
     * @param key The [ResultKey] identifying the result
     * @param value The result value to store, or null to remove the result
     *
     * @see getResult to retrieve the stored value
     * @see removeResult to remove a result explicitly
     * @see getResultFlow to observe result changes
     */
    fun setResult(key: ResultKey, value: Any?) {
        val callbacks = lock.withLock {
            if (value != null) {
                resultMap[key] = value
            } else {
                resultMap.remove(key)
            }
            resultCallbacks[key]
        }
        // Keep callback outside of lock in case a callback tries to acquire the lock.
        callbacks?.forEach { it.onResult(value) }
    }

    /**
     * Removes an untyped result from the store and notifies all registered listeners.
     *
     * All listeners are invoked with null to indicate the result has been removed. This method
     * is thread-safe and uses internal locking to ensure the result and callback lists are
     * accessed consistently. Callbacks are invoked outside the lock to prevent deadlocks.
     *
     * @param key The [ResultKey] identifying the result to remove
     * @return The removed result as Any, or null if no result was stored for the given key
     *
     * @see removeResult for type-safe removal
     * @see setResult to store a result
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun removeResultValue(key: ResultKey): Any? {
        val (result, callbacks) = lock.withLock {
            val result = resultMap.remove(key)
            val callbacks = resultCallbacks[key]
            result to callbacks
        }
        // Keep callback outside of lock in case a callback tries to acquire the lock.
        callbacks?.forEach { it.onResult(null) }
        return result
    }


    /**
     * Removes a type-safe result from the store and notifies all registered listeners.
     *
     * All listeners are invoked with null to indicate the result has been removed.
     * This method is thread-safe and uses internal locking to ensure consistent access.
     *
     * @param T The expected type of the result to remove
     * @param key The [ResultKey] identifying the result to remove
     * @return The removed result cast to type [T], or null if no result exists or type mismatch occurs
     *
     * @see setResult to store a result
     * @see removeResultValue for untyped removal
     * @see getAndRemoveResult for atomic get-and-remove operations
     */
    inline fun <reified T : Any> removeResult(key: ResultKey): T? {
        return removeResultValue(key) as T?
    }

    /**
     * Retrieves and removes an untyped result from the store in a single atomic operation.
     *
     * This is a convenience method that calls [removeResultValue].
     * Registered listeners are notified that the result has been removed.
     *
     * @param key The [ResultKey] identifying the result
     * @return The removed result as Any, or null if no result was stored for the given key
     *
     * @see removeResultValue for detailed behavior
     * @see getAndRemoveResult for type-safe get-and-remove
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAndRemoveResultValue(key: ResultKey): Any? {
        return removeResultValue(key)
    }

    /**
     * Retrieves and removes a type-safe result from the store in a single atomic operation.
     *
     * This is a convenience method that calls [removeResultValue] and casts the result to type [T].
     * Registered listeners are notified that the result has been removed.
     *
     * @param T The expected type of the result to retrieve and remove
     * @param key The [ResultKey] identifying the result
     * @return The removed result cast to type [T], or null if no result exists or type mismatch occurs
     *
     * @see removeResult for type-safe removal without immediate retrieval
     * @see getAndRemoveResultValue for untyped get-and-remove
     */
    inline fun <reified T : Any> getAndRemoveResult(key: ResultKey): T? {
        return removeResultValue(key) as T?
    }

    /**
     * Registers a listener to be notified of result changes for the given key.
     *
     * The callback is immediately invoked with the current result (if any) when registered.
     * Subsequently, the callback will be invoked whenever the result changes via [setResult] or
     * is removed via [removeResultValue].
     *
     * @param key The [ResultKey] identifying the result to listen for
     * @param callback The [ResultStoreCallback] to invoke on result changes
     */
    private fun registerListener(key: ResultKey, callback: ResultStoreCallback) {
        val result = lock.withLock {
            resultCallbacks.getOrPut(key) { mutableListOf() }.add(callback)
            resultMap[key]
        }
        // Keep callback outside of lock in case a callback tries to acquire the lock.
        callback.onResult(result)
    }

    /**
     * Unregisters a listener from receiving further result change notifications.
     *
     * After unregistering, the callback will no longer be invoked for result changes.
     * If this was the last listener for the given key, the callback list is removed from the store.
     *
     * @param key The [ResultKey] identifying the result
     * @param callback The [ResultStoreCallback] to unregister
     */
    private fun unregisterListener(key: ResultKey, callback: ResultStoreCallback) {
        lock.withLock {
            val list = resultCallbacks[key] ?: return
            list.remove(callback)
            if (list.isEmpty()) {
                resultCallbacks.remove(key)
            }
        }
    }

    /**
     * Returns an untyped Flow that emits result changes for the given key.
     *
     * The Flow emits the current result (if any) immediately upon collection, or null if no result
     * is currently stored. Subsequent emissions occur whenever the result changes via [setResult]
     * or is removed. The Flow completes when the collector stops collecting or an unrecoverable
     * error occurs.
     *
     * This method is thread-safe and uses internal listener registration and unregistration
     * to track active subscribers.
     *
     * @param key The [ResultKey] identifying the result to observe
     * @return A Flow that emits result values (or null if no result is stored or after removal)
     *
     * @see getResultFlow for type-safe flow observations
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getResultValueFlow(key: ResultKey): Flow<Any?> {
        return callbackFlow {
            val callback = object : ResultStoreCallback {
                override fun onResult(result: Any?) {
                    trySend(result)
                }
            }
            registerListener(key, callback)

            awaitClose { unregisterListener(key, callback) }
        }

    }

    /**
     * Returns a type-safe Flow that emits result changes for the given key.
     *
     * The Flow emits the current result (if any) immediately upon collection, cast to type [T],
     * or null if no result is currently stored or if the stored value is not compatible with type [T].
     * Subsequent emissions occur whenever the result changes via [setResult] or is removed.
     * The Flow completes when the collector stops collecting or an unrecoverable error occurs.
     *
     * Type mismatches are handled gracefully by emitting null rather than throwing exceptions.
     *
     * @param T The expected type of the result to observe
     * @param key The [ResultKey] identifying the result to observe
     * @return A Flow that emits result changes cast to type [T] (or null if no result is stored,
     *         type mismatch occurs, or the result is removed)
     *
     * @see getResultValueFlow for untyped flow observations
     */
    inline fun <reified T : Any> getResultFlow(key: ResultKey): Flow<T?> {
        return getResultValueFlow(key).map { it as? T }
    }
}

/**
 * A marker interface for result store keys.
 *
 * Implementations of [ResultKey] serve as unique identifiers for results stored in the [ResultStore].
 * Keys are typically implemented as singletons or data objects to ensure consistency across the application.
 *
 * Example:
 * ```
 * object MyResultKey : ResultKey
 * ```
 */
interface ResultKey

/**
 * Private callback interface for result change notifications.
 *
 * [ResultStoreCallback] is used internally by [ResultStore] to notify listeners when result values change.
 * Implementations of this interface are created dynamically by the store to bridge between listener
 * registrations and Flow emissions.
 *
 * @see ResultStore.registerListener
 * @see ResultStore.getResultValueFlow
 */
private interface ResultStoreCallback {
    /**
     * Called when a result is updated or removed.
     *
     * @param result The new result value, or null if the result was removed or no result exists
     */
    fun onResult(result: Any?)
}
