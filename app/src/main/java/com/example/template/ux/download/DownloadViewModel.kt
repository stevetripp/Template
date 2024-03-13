package com.example.template.ux.download

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.template.churchmedia.MediaDownloadUtil
import com.example.template.ext.stateInDefault
import com.example.template.work.DownloadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel
@Inject constructor(
    application: Application,
    private val mediaDownloadUtil: MediaDownloadUtil,
) : ViewModel() {

    private val workManager: WorkManager = WorkManager.getInstance(application)
    private val downloadIdFlow = MutableStateFlow("")
    private val downloadStateIdFlow = MutableStateFlow<String?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val downloadStateFlow = downloadStateIdFlow.filterNotNull().flatMapLatest {
        Log.i("SMT", "downloadStateFlow($it)")
        mediaDownloadUtil.getDownloadProgressForeverFlow(it, pollInterval = 100)
    }.stateInDefault(viewModelScope, null)

    val uiState = DownloadUiState(
        downloadIdFlow = downloadIdFlow,
        downloadProgressFlow = downloadStateFlow,
        onDownloadIdChanged = { downloadIdFlow.value = it },
        onDownload = ::onDownload,
        onRemove = ::onRemove
    )

    private fun onRemove() {
        mediaDownloadUtil.removeDownload(downloadIdFlow.value)
    }

    private fun onDownload() {
        val inputData = DownloadWorker.createInputDataBuilder(downloadIdFlow.value).build()
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequestBuilder = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
        workManager.enqueue(workRequestBuilder.build())
        downloadStateIdFlow.value = downloadIdFlow.value
    }
}