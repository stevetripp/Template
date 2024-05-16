package org.lds.media.cast

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener

open class CastSessionListener : SessionManagerListener<CastSession> {

    override fun onSessionStarting(castSession: CastSession) {
        onCastDeviceConnecting()
    }

    override fun onSessionStarted(castSession: CastSession, sessionId: String) {
        onCastDeviceConnected(false)
    }

    override fun onSessionStartFailed(castSession: CastSession, error: Int) {
        onCastDeviceDisconnected()
    }

    override fun onSessionEnding(castSession: CastSession) {
        // Purposely left blank
    }

    override fun onSessionEnded(castSession: CastSession, error: Int) {
        onCastDeviceDisconnected()
    }

    override fun onSessionResuming(castSession: CastSession, sessionId: String) {
        onCastDeviceConnecting()
    }

    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) {
        onCastDeviceConnected(true)
    }

    override fun onSessionResumeFailed(castSession: CastSession, error: Int) {
        onCastDeviceDisconnected()
    }

    override fun onSessionSuspended(castSession: CastSession, reason: Int) {
        onCastDeviceDisconnected()
    }

    /**
     * A utility method that will handle the generic "connected" events by allowing extending classes to only override one method
     * @param resumed - true if the cast sessions was resumed
     */
    open fun onCastDeviceConnected(resumed: Boolean) {
        // Purposely left blank
    }

    /**
     * A utility method that will handle the generic "connecting" events by allowing extending classes to only override one method
     */
    fun onCastDeviceConnecting() {
        // Purposely left blank
    }

    /**
     * A utility method that will handle the generic "disconnected" events by allowing extending classes to only override one method
     */
    open fun onCastDeviceDisconnected() {
        // Purposely left blank
    }
}
