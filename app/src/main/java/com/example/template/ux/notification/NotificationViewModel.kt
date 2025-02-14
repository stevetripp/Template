package com.example.template.ux.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUtil: NotificationUtil,
) : ViewModel() {
    val uiState = NotificationUiState(
        onCreateNotification = ::onCreateNotification,
        onCreateDeepLinkNotification = ::onCreateDeepLinkNotification
    )

    private fun onCreateNotification(context: Context) {
        notificationUtil.showNotification(context)
    }

    private fun onCreateDeepLinkNotification(context: Context) {
        notificationUtil.showDeepLinkNotification(context)
    }
}