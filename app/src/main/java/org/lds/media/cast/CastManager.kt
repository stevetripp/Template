package org.lds.media.cast

import android.content.Context
import android.view.Menu
import android.view.View
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class CastManager(context: Context, initCast: Boolean, var castFromDeviceEnabled: Boolean = false) {

    var castContext: CastContext? = null
        set
    var castEnabled: Boolean = false

    val isConnected: Boolean
        get() = castContext?.sessionManager?.currentCastSession?.isConnected ?: false

    val isConnecting: Boolean
        get() = castContext?.sessionManager?.currentCastSession?.isConnecting ?: false

    val isDisconnected: Boolean
        get() = !isConnected

    val currentCastSession: CastSession?
        get() = castContext?.sessionManager?.currentCastSession

    private val context: Context = context.applicationContext
    private val castPlaybackCallbackList: MutableList<CastPlaybackCallback> = ArrayList()

    init {
        init(context, initCast)
    }

    /**
     * Detaches the currently connected Google Cast device, stopping
     * any playback on the device.
     */
    fun detach() {
        castContext?.let {
            castContext?.sessionManager?.endCurrentSession(true)
        }
    }

    fun registerCastSessionListener(castSessionListener: CastSessionListener) {
        castContext?.sessionManager?.addSessionManagerListener(castSessionListener, CastSession::class.java)
    }

    fun unregisterCastSessionListener(castSessionListener: CastSessionListener) {
        castContext?.sessionManager?.removeSessionManagerListener(castSessionListener, CastSession::class.java)
    }

    /**
     * Adds the callback listener to be informed of any Google Cast
     * changes.
     *
     * @param playbackCallback The playbackCallback to be informed
     */
    fun addCastPlaybackCallback(playbackCallback: CastPlaybackCallback?) {
        if (playbackCallback != null) {
            castPlaybackCallbackList.add(playbackCallback)
        }
    }

    /**
     * Removes a previously registered callback
     *
     * @param playbackCallback The playbackCallback to be removed
     */
    fun removeCastPlaybackCallback(playbackCallback: CastPlaybackCallback?) {
        if (playbackCallback == null) {
            return
        }

        val iterator = castPlaybackCallbackList.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() == playbackCallback) {
                iterator.remove()
            }
        }
    }

    fun notifyCastPlaybackStarted() {
        for (castCallback in castPlaybackCallbackList) {
            castCallback.onCastPlaybackStarted()
        }
    }

    fun notifyCastPlaybackPaused() {
        for (castCallback in castPlaybackCallbackList) {
            castCallback.onCastPlaybackPaused()
        }
    }

    fun notifyCastPlaybackFinished() {
        for (castCallback in castPlaybackCallbackList) {
            castCallback.onCastPlaybackFinished()
        }
    }

    fun notifyCastPlaybackPrepared() {
        for (castCallback in castPlaybackCallbackList) {
            castCallback.onCastPlaybackPrepared()
        }
    }

    fun setupCastButton(context: Context, mediaRouteButton: androidx.mediarouter.app.MediaRouteButton): Boolean {
        val isCastAvailable = castContext != null
        castContext?.let {
            CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton)
        }
        mediaRouteButton.visibility = if (isCastAvailable) View.VISIBLE else View.GONE
        return isCastAvailable
    }

    fun setupCastButton(context: Context, menu: Menu, menuResourceId: Int): Boolean {
        val isCastAvailable = castContext != null
        castContext?.let {
            CastButtonFactory.setUpMediaRouteButton(context, menu, menuResourceId)
        }
        menu.findItem(menuResourceId).isVisible = isCastAvailable
        return isCastAvailable
    }

    fun enableCast(enabled: Boolean) {
        if (castEnabled == enabled) {
            return
        }

        if (castEnabled) {
            // if cast is currently enabled then disable the cast
            detach()
            castPlaybackCallbackList.clear()

            castContext = null
        } else {
            // if cast is currently disabled then initialize (enable) the cast
            init(context, true)
        }

        castEnabled = enabled
    }

    private fun init(context: Context, initCast: Boolean) {
        if (!initCast) {
            return
        }

        // CastContext.getSharedInstance could throw an exception if they are not using the
        // latest play services version. If it does, just disable casting.
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        if (googleApiAvailability.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            try {
                castContext = CastContext.getSharedInstance(context)
                castEnabled = true
            } catch (ignore: Exception) {
                castEnabled = false
            }
        }
    }

    companion object {
        const val KEY_LIVESTREAM = "Livestream"
    }
}