package com.example.template.ux.gmailaddressfield

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ui.widget.InputChip
import com.example.template.ux.main.Screen
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
fun GmailAddressFieldScreen(navController: NavController, viewModel: GmailAddressFieldViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    GmailAddressFieldContent(uiState, navController::navigateUp)
}

@Composable
private fun GmailAddressFieldContent(uiState: GmailAddressFieldUiState, onBack: () -> Unit = {}) {
    val contacts by uiState.contactsFlow.collectAsStateWithLifecycle()
    val chips by uiState.chipsFlow.collectAsStateWithLifecycle()

    Log.i("SMT", "GmailAddressFieldContent")
    if (!LocalInspectionMode.current) {

        val readContactsPermissionState = rememberPermissionState(android.Manifest.permission.READ_CONTACTS)

        LaunchedEffect(key1 = readContactsPermissionState.status) {
            Log.i("SMT", "LaunchedEffect: ${readContactsPermissionState.status}")
            if (readContactsPermissionState.status.isGranted) {
                uiState.setContactsReadPermission(true)
            } else {
                readContactsPermissionState.launchPermissionRequest()
            }
        }
    }

    var value by remember { mutableStateOf("") }

    Scaffold(topBar = { AppTopAppBar(title = Screen.GMAIL_ADDRESS_FIELD.title, onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            FlowRow(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                chips.forEach { contact ->
                    InputChip(
                        label = contact.name,
                        leadingIcon = {
                            AsyncImage(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Magenta),
                                model = contact.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                            )
                        },
                        onRemove = {},
                    )
                }
                BasicTextField(
                    modifier = Modifier.height(30.dp),
                    value = value,
                    onValueChange = { value = it },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                if (value.isEmpty()) Text(text = "Placeholder Text")
                                innerTextField()
                            }
                        }
                    },
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .weight(1F)
            ) {
                items(contacts) { contact ->
                    ListItem(
                        modifier = Modifier.clickable { uiState.onContactClicked(contact) },
                        leadingContent = {
                            AsyncImage(
                                modifier = Modifier.clip(CircleShape),
                                model = contact.imageUrl, contentDescription = null,
                                contentScale = ContentScale.Crop,
                            )
                        },
                        headlineContent = { Text(text = contact.name) },
                        supportingContent = { Text(text = contact.emailAddress) }
                    )
                }
            }
        }
    }
}

@PreviewDefault
@Composable
private fun GmailAddressFieldContentPreview() {
    val text = "Proin eget tortor risus."
    val random = Random(4L)
    val contacts = List(10) {
        val name = text.substring((0..random.nextInt(text.length)))
        Contact("", "$name $it", "contact$1@domain.com")
    }
    AppTheme {
        GmailAddressFieldContent(GmailAddressFieldUiState(contactsFlow = MutableStateFlow(contacts), chipsFlow = MutableStateFlow(contacts)))
    }
}