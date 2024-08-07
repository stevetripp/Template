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

    private val reqParam1Flow = MutableStateFlow("")
    private val reqParam2Flow = MutableStateFlow(EnumParameter.ONE)
    private val optParam1Flow = MutableStateFlow<String?>(null)
    private val optParam2Flow = MutableStateFlow<EnumParameter?>(null)

    val uiState = ParametersUiState(
        reqParam1Flow = reqParam1Flow,
        reqParam2Flow = reqParam2Flow,
        optParam1Flow = optParam1Flow,
        optParam2Flow = optParam2Flow,
        onReqParam1Changed = { reqParam1Flow.value = it },
        onReqParam2Changed = { reqParam2Flow.value = it },
        onOptParam1Changed = { optParam1Flow.value = it },
        onOptParam2Changed = { optParam2Flow.value = it },
        onButtonClick = ::onButtonClick
    )

    private fun onButtonClick() {
        if (reqParam1Flow.value.isBlank()) return
        navigate(
            DestinationRoute(
                reqParam1 = Parameter1(reqParam1Flow.value),
                reqParam2 = reqParam2Flow.value,
                optParam1 = optParam1Flow.value?.let { Parameter1(it) },
                optParam2 = optParam2Flow.value
            )
        )
    }
}