package com.example.template.shared.util.file

import android.app.Application
import okio.Path
import okio.Path.Companion.toOkioPath

actual object AppFileSystem {
    lateinit var applicationContext: Application

    actual fun getDatastoreDir(): Path = getFilesDir() / "datastore"
    actual fun getDatastorePath(name: String): Path = getDatastoreDir() / "$name.preferences_pb"
    actual fun getExternalFilesDir(type: String?): Path? = applicationContext.getExternalFilesDir(type)?.toOkioPath()
    actual fun getFilesDir(): Path = applicationContext.filesDir.toOkioPath()
}
