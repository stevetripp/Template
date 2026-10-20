package org.lds.mobile.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun withLifecycleOwner(lifecycleOwner: LifecycleOwner, block: LifecycleGroup.() -> Unit) {
    LifecycleGroup(lifecycleOwner).run(block)
}

class LifecycleGroup(val lifecycleOwner: LifecycleOwner) {
    /**
     * Recommended for when every emit needs to be processed to completion.
     */
    inline fun <T> Flow<T>.collectWhenStarted(crossinline block: suspend (T) -> Unit): Job {
        return collectWhen(Lifecycle.State.STARTED, block)
    }

    /**
     * Recommended for the majority of use cases
     * The crucial difference from [collect] is that when the original flow emits a new value, [block] for previous
     * value is cancelled.
     **/
    inline fun <T> Flow<T>.collectLatestWhenStarted(crossinline block: suspend (T) -> Unit): Job {
        return collectLatestWhen(Lifecycle.State.STARTED, block)
    }

    /**
     * Recommended for collecting inside a fragment in a viewpager and you need to process every emit to completion
     */
    inline fun <T> Flow<T>.collectWhenResumed(crossinline block: suspend (T) -> Unit): Job {
        return collectWhen(Lifecycle.State.RESUMED, block)
    }

    /**
     * Recommended for collecting inside a fragment in a viewpager when only the active fragment should receive events.
     * If fragments need to be updated in the background use [collectLatestWhenStarted]
     */
    inline fun <T> Flow<T>.collectLatestWhenResumed(crossinline block: suspend (T) -> Unit): Job {
        return collectLatestWhen(Lifecycle.State.RESUMED, block)
    }

    /**
     * Recommended for the majority of use cases
     */
    inline fun <T> ReceiveChannel<T>.receiveWhenStarted(crossinline block: suspend (T) -> Unit): Job {
        return receiveWhen(Lifecycle.State.STARTED, block)
    }

    /**
     * Recommended for receiving inside a fragment in a viewpager as only the active Fragment will be affected.
     */
    inline fun <T> ReceiveChannel<T>.receiveWhenResumed(crossinline block: suspend (T) -> Unit): Job {
        return receiveWhen(Lifecycle.State.RESUMED, block)
    }

    /**
     * Recommended for when every emit needs to be processed to completion.
     */
    inline fun <T> Flow<T>.collectWhen(state: Lifecycle.State, crossinline block: suspend (T) -> Unit): Job {
        return lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(state) {
                this@collectWhen.collect {
                    block(it)
                }
            }
        }
    }

    /**
     * Recommended for the majority of use cases
     * The crucial difference from [collect] is that when the original flow emits a new value, [block] for previous
     * value is cancelled.
     **/
    inline fun <T> Flow<T>.collectLatestWhen(state: Lifecycle.State, crossinline block: suspend (T) -> Unit): Job {
        return lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(state) {
                this@collectLatestWhen.collectLatest {
                    block(it)
                }
            }
        }
    }

    /**
     * Recommended for receiving inside a fragment in a viewpager as only the active Fragment will be affected.
     */
    inline fun <T> ReceiveChannel<T>.receiveWhen(state: Lifecycle.State, crossinline block: suspend (T) -> Unit): Job {
        return lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(state) {
                for (e in this@receiveWhen) {
                    block(e)
                }
            }
        }
    }
}