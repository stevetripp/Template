package com.example.template.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.template.churchmedia.MediaDownloadUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class DownloadWorker
@AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mediaDownloadUtil: MediaDownloadUtil,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val keyId = inputData.getString(KEY_ID) ?: return Result.failure()
        val keyUrl = urls[keyId] ?: return Result.failure()

        Log.i(
            "SMT", """DownloadWorker doing  work ...
            |keyId: $keyId
            |keyUrl: $keyUrl
        """.trimMargin()
        )

        mediaDownloadUtil.addDownload(keyId, keyUrl)

        Log.i("SMT", "DownloadWorker done!")

        return Result.success()
    }

    companion object {
        private val urls = mapOf(
            "1" to "https://media2.ldscdn.org/assets/general-conference/october-2023-general-conference/2023-10-1010-david-a-bednar-1080p-eng.mp4",
            "2" to "https://media2.ldscdn.org/assets/general-conference/october-2023-general-conference/2023-10-1020-amy-a-wright-1080p-eng.mp4",
            "3" to "https://media2.ldscdn.org/assets/general-conference/october-2023-general-conference/2023-10-1050-d-todd-christofferson-1080p-eng.mp4",
        )

        private const val KEY_ID = "KEY_ID"

        fun createInputDataBuilder(id: String): Data.Builder {
            return Data.Builder()
                .putString(KEY_ID, id)
        }
    }
}