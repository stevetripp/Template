package org.lds.mobile.download.direct

data class DirectDownloadResult(
    val success: Boolean,
    val message: String? = null,
    val code: Int = -1
)