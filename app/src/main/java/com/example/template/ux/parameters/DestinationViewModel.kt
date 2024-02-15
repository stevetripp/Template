package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = DestinationRoute.getArgs(savedStateHandle)
    private val requiredFlow = MutableStateFlow(args.param1.value)
    private val optionalFlow = MutableStateFlow(args.param2?.value)

    val uiState = DestinationUiState(
        requiredFlow = requiredFlow,
        optionalFlow = optionalFlow,
    )
}