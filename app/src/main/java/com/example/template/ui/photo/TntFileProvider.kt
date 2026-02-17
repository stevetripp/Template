package com.example.template.ui.photo

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.template.BuildConfig
import com.example.template.R
import java.io.File

class TntFileProvider : FileProvider(R.xml.filepaths) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()

            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            return getUriForFile(
                context,
                BuildConfig.FILE_PROVIDER,
                file,
            )
        }
    }
}
