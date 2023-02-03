package com.example.template.ux.parameters

import androidx.lifecycle.ViewModel
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ParametersViewModel
@Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val requiredValueFlow = MutableStateFlow("")
    private val optionalValueFlow = MutableStateFlow("")

    val uiState = ParametersUiState(
        requiredValueFlow = requiredValueFlow,
        optionalValueFlow = optionalValueFlow,
        onRequiredValueChanged = { requiredValueFlow.value = it },
        onOptionalValueChanged = { optionalValueFlow.value = it },
        onButtonClick = { navigate(DestinationRoute.createRoute(requiredValueFlow.value, optionalValueFlow.value)) }
    )
}