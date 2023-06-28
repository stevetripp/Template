package com.example.template.ux.imagepicker

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.photo.TntFileProvider
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen

@Composable
fun ImagePickerScreen(navController: NavController) {
    ImagePickerContent(navController::popBackStack)
}

@Composable
fun ImagePickerContent(onBack: () -> Unit = {}) {
    Scaffold(topBar = { AppTopAppBar(title = Screen.IMAGE_PICKER.title, onBack = onBack) }) {
        val context = LocalContext.current
        var photoUri by remember { mutableStateOf<Uri?>(null) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val bitmap = remember { mutableStateOf<Bitmap?>(null) }

        val photoCaptureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { isSuccessful ->
            if (isSuccessful) {
                imageUri = photoUri
            }
        }

        val photoGalleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uri ->
            imageUri = uri.first()
        }

        Column(modifier = Modifier.padding(it)) {
            Row {
                Button(onClick = {
                    photoGalleryLauncher.launch("image/*")
                }) {
                    Text(text = "Pick image")
                }

                Button(onClick = {
                    photoUri = TntFileProvider.getImageUri(context)
                    photoCaptureLauncher.launch(photoUri)
                }) {
                    Text(text = "Take picture")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            imageUri?.let { uri ->
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun ImagePickerContentPreview() {
    AppTheme { ImagePickerContent() }
}