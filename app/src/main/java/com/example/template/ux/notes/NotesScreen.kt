package com.example.template.ux.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.shared.model.domain.Note
import com.example.template.ui.PreviewDefault
import com.example.template.ui.composable.AppTopAppBar
import com.example.template.ui.theme.AppTheme
import com.example.template.ux.main.Screen
import kotlin.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.navigator.Navigation3Navigator

@Composable
fun NotesScreen(navigator: Navigation3Navigator, viewModel: NotesViewModel) {
    NotesContent(viewModel.uiState, navigator::pop)
}

@Composable
fun NotesContent(uiState: NotesUiState, onBack: () -> Unit = {}) {
    val notes by uiState.notesFlow.collectAsStateWithLifecycle()
    val newNoteText by uiState.newNoteTextFlow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = Screen.ROOM_DATABASE.title,
                onBack = onBack,
                actions = {
                    IconButton(onClick = uiState.onSeedNotes) {
                        Icon(imageVector = Icons.AutoMirrored.Default.PlaylistAdd, contentDescription = "Add sample notes")
                    }
                    IconButton(onClick = uiState.onDeleteDone, enabled = notes.any { it.isDone }) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "Delete completed notes")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = newNoteText,
                    onValueChange = uiState.onNewNoteTextChange,
                    placeholder = { Text(text = "New note") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { uiState.onAddNote() }),
                )
                IconButton(onClick = uiState.onAddNote, enabled = newNoteText.isNotBlank()) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                }
            }

            if (notes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "No notes yet. Notes are stored in a Room database and survive app restarts.", textAlign = TextAlign.Center)
                    Button(onClick = uiState.onSeedNotes) {
                        Text(text = "Add Sample Notes")
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(notes, key = { it.id }) { note ->
                        NoteItem(
                            note = note,
                            onToggleDone = { uiState.onToggleDone(note) },
                            onDelete = { uiState.onDelete(note) },
                            modifier = Modifier.animateItem(),
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteItem(note: Note, onToggleDone: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier.clickable(onClick = onToggleDone),
        leadingContent = { Checkbox(checked = note.isDone, onCheckedChange = { onToggleDone() }) },
        headlineContent = {
            Text(
                text = note.text,
                textDecoration = if (note.isDone) TextDecoration.LineThrough else null,
                color = if (note.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
            }
        },
    )
}

@PreviewDefault
@Composable
private fun Preview() {
    val created = Instant.fromEpochMilliseconds(0)
    val uiState = NotesUiState(
        notesFlow = MutableStateFlow(
            listOf(
                Note(id = 1, text = "Buy milk", isDone = false, created = created),
                Note(id = 2, text = "Walk the dog", isDone = false, created = created),
                Note(id = 3, text = "Write the Room example", isDone = true, created = created),
            )
        ),
        newNoteTextFlow = MutableStateFlow("Call mom"),
    )
    AppTheme { NotesContent(uiState) }
}
