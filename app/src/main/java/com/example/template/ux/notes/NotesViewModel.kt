package com.example.template.ux.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.shared.model.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.lds.mobile.ext.stateInDefault

class NotesViewModel(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val newNoteTextFlow = MutableStateFlow("")

    val uiState = NotesUiState(
        notesFlow = noteRepository.notesFlow.stateInDefault(viewModelScope, emptyList()),
        newNoteTextFlow = newNoteTextFlow,
        onNewNoteTextChange = { newNoteTextFlow.value = it },
        onAddNote = ::addNote,
        onSeedNotes = { viewModelScope.launch { noteRepository.seedNotes() } },
        onToggleDone = { note -> viewModelScope.launch { noteRepository.setDone(note.id, !note.isDone) } },
        onDelete = { note -> viewModelScope.launch { noteRepository.delete(note.id) } },
        onDeleteDone = { viewModelScope.launch { noteRepository.deleteDone() } },
    )

    private fun addNote() {
        val text = newNoteTextFlow.value
        viewModelScope.launch {
            if (noteRepository.addNote(text) != null) {
                newNoteTextFlow.value = ""
            }
        }
    }
}
