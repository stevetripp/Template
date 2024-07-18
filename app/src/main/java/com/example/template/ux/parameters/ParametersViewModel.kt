package com.example.template.ux.parameters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class ParametersViewModel
@Inject constructor() : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val requiredValueFlow = MutableStateFlow("")
    private val enumParameterFlow = MutableStateFlow<EnumParameter>(EnumParameter.ONE)
    private val optionalValueFlow = MutableStateFlow<String?>(null)

    val uiState = ParametersUiState(
        requiredValueFlow = requiredValueFlow,
        enumParameterFlow = enumParameterFlow,
        optionalValueFlow = optionalValueFlow,
        onRequiredValueChanged = { requiredValueFlow.value = it },
        onEnumParameterChanged = { enumParameterFlow.value = it },
        onOptionalValueChanged = { optionalValueFlow.value = it },
        onButtonClick = ::onButtonClick
    )

    private fun onButtonClick() {
        if (requiredValueFlow.value.isBlank()) return
        navigate(DestinationRoute(Parameter1(requiredValueFlow.value), enumParameterFlow.value, optionalValueFlow.value?.let { Parameter2(it) }))
    }
}