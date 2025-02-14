package com.example.template.ux.notification

import android.content.Context

data class NotificationUiState(
    val onCreateNotification: (Context) -> Unit = {},
    val onCreateDeepLinkNotification: (Context) -> Unit = {},
)