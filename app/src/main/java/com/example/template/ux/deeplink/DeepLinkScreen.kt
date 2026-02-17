package com.example.template.ux.deeplink

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.template.ui.PreviewDefault
import com.example.template.ui.PreviewNavigator
import com.example.template.util.DeepLinkConstants
import com.example.template.ux.main.Screen
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun DeepLinkScreen(
    navigator: Navigation3Navigator,
    route: DeepLinkRoute,
) {
    val adbCommand =
        "adb shell am start -a android.intent.action.VIEW -d \"${DeepLinkConstants.CUSTOM_ROOT}/DEEPLINK/{requiredValue}?optionalValue={optionalValue}\""
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Screen.DEEP_LINK.title) },
                navigationIcon = {
                    IconButton(onClick = navigator::pop) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(adbCommand))
                        Toast.makeText(context, "Command copied to clipboard", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Outlined.ContentCopy, contentDescription = "Copy command")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Deep linking can be tested with the following adb command:")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = adbCommand, modifier = Modifier.padding(end = 8.dp))
            }

            OutlinedTextField(
                value = route.requiredValue,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Required path value") }
            )

            OutlinedTextField(
                value = route.optionalValue.orEmpty(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Optional query value") },
                placeholder = { Text("Not provided") }
            )
        }
    }
}

@PreviewDefault
@Composable
private fun DeepLinkScreenPreview() {
    DeepLinkScreen(
        navigator = PreviewNavigator(),
        route = DeepLinkRoute(
            requiredValue = "samplePath",
            optionalValue = "sampleQuery"
        )
    )
}
