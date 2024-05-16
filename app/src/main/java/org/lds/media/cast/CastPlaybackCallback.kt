package org.lds.media.cast

/**
 * Callbacks used for the LdsCast object
 */
open class CastPlaybackCallback {

    /**
     * Called when the LdsCast starts the playback
     */
    open fun onCastPlaybackStarted() {
        // Purposely left blank
    }

    /**
     * Called when the LdsCast pause the playback
     */
    open fun onCastPlaybackPaused() {
        // Purposely left blank
    }

    /**
     * Called when the current playback has completed
     */
    fun onCastPlaybackFinished() {
        // Purposely left blank
    }

    /**
     * Called when the current playback has been prepared (loaded).
     */
    fun onCastPlaybackPrepared() {
        // Purposely left blank
    }
}
