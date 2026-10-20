package com.example.template.ux.notes

import com.example.template.shared.model.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class NotesUiState(
    val notesFlow: StateFlow<List<Note>> = MutableStateFlow(emptyList()),
    val newNoteTextFlow: StateFlow<String> = MutableStateFlow(""),
    val onNewNoteTextChange: (String) -> Unit = {},
    val onAddNote: () -> Unit = {},
    val onSeedNotes: () -> Unit = {},
    val onToggleDone: (Note) -> Unit = {},
    val onDelete: (Note) -> Unit = {},
    val onDeleteDone: () -> Unit = {},
)
