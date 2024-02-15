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
    private val optionalValueFlow = MutableStateFlow<String?>(null)

    val uiState = ParametersUiState(
        requiredValueFlow = requiredValueFlow,
        optionalValueFlow = optionalValueFlow,
        onRequiredValueChanged = { requiredValueFlow.value = it },
        onOptionalValueChanged = { optionalValueFlow.value = it },
        onButtonClick = ::onButtonClick
    )

    private fun onButtonClick() {
        if (requiredValueFlow.value.isBlank()) return
        navigate(DestinationRoute.createRoute(Parameter1(requiredValueFlow.value), optionalValueFlow.value?.let { Parameter2(it) }))
    }
}