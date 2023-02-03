package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val requiredFlow = savedStateHandle.getStateFlow(DestinationRoute.Args.REQUIRED, "")
    private val optionalFlow = savedStateHandle.getStateFlow<String?>(DestinationRoute.Args.OPTIONAL, null)

    val uiState = DestinationUiState(
        requiredFlow = requiredFlow,
        optionalFlow = optionalFlow,
    )
}