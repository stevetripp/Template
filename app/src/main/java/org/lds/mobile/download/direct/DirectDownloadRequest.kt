package org.lds.mobile.download.direct

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.util.UUID

data class DirectDownloadRequest (
    val downloadUrl: String,
    val fileSystem: FileSystem,
    val targetFile: Path,
    val id: String = UUID.randomUUID().toString(),
    val overwriteExisting: Boolean = true,
    val customHeaders: List<DirectDownloadHeader>? = null
) {
    @Deprecated("Use Okio DirectDownloadRequest constructor instead (using FileSystem and Path)")
    constructor(
        downloadUrl: String,
        targetFile: File,
        id: String = UUID.randomUUID().toString(),
        overwriteExisting: Boolean = true,
        customHeaders: List<DirectDownloadHeader>? = null
    ) : this(
        downloadUrl,
        FileSystem.SYSTEM,
        targetFile.toOkioPath(),
        id,
        overwriteExisting,
        customHeaders
    )
}

data class DirectDownloadHeader(val name: String, val value: String)