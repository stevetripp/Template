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
    private val requiredFlow = MutableStateFlow(destinationRoute.param1)
    private val enumParameterFlow = MutableStateFlow(destinationRoute.enumParam)
    private val optionalFlow = MutableStateFlow(destinationRoute.param2)
    private val optionEnumParameterFlow = MutableStateFlow(destinationRoute.optionalEnumParam)

    val uiState = DestinationUiState(
        requiredFlow = requiredFlow,
        enumParameterFlow = enumParameterFlow,
        optionalFlow = optionalFlow,
        optionalEnumParameterFlow = optionEnumParameterFlow,
    )
}