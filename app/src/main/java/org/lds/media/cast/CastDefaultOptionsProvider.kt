package org.lds.media.cast

import android.content.Context

import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

// Default production-ready custom receiver. This should be implemented in the AndroidManifest.xml of the sender application
class CastDefaultOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setReceiverApplicationId(CastApplication.CHURCH_PRODUCTION_RECEIVER.appId)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}

// Stage custom receiver. Used for testing the custom receiver before deploying to production
class CastStageOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setReceiverApplicationId(CastApplication.CHURCH_STAGE_RECEIVER.appId)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}

// Test custom receiver. Used for testing the custom receiver through the test application
class CastTestOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setReceiverApplicationId(CastApplication.CHURCH_DEV_RECEIVER.appId)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}

enum class CastApplication(val appId: String, val url: String) {
    CHURCH_PRODUCTION_RECEIVER("7669F508", "https://cdn.churchofjesuschrist.org/cdn2/esm/cast-receiver/receiver.html"),
    CHURCH_STAGE_RECEIVER("C8B0EE9C", "https://stage.ldscdn.org/cdn2/esm/cast-receiver/receiver.html"),
    CHURCH_DEV_RECEIVER("FE816BC5", "https://cdnlocal.churchofjesuschrist.org/mobile/cast/receiver.html"),
    DEFAULT_RECEIVER(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID, "")
}