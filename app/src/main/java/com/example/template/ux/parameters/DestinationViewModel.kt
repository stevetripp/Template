package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val destinationRoute = savedStateHandle.toRoute<DestinationRoute>(DestinationRoute.typeMap())
    private val requiredFlow = MutableStateFlow(destinationRoute.param1.value)
    private val optionalFlow = MutableStateFlow(destinationRoute.param2?.value)

    val uiState = DestinationUiState(
        requiredFlow = requiredFlow,
        optionalFlow = optionalFlow,
    )
}