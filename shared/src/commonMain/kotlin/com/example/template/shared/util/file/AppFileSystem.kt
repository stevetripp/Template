package com.example.template.shared.util.file

import okio.Path

expect object AppFileSystem {
    fun getDatastoreDir(): Path
    fun getDatastorePath(name: String): Path
    fun getExternalFilesDir(type: String?): Path?
    fun getFilesDir(): Path
}
