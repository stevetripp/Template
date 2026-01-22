package com.example.template.ux.parameters

import androidx.lifecycle.ViewModel
import com.example.template.domain.Parameter
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

class ParametersViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

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
                reqParam1 = Parameter(reqParam1Flow.value),
                reqParam2 = reqParam2Flow.value,
                optParam1 = optParam1Flow.value?.let { Parameter(it) },
                optParam2 = optParam2Flow.value
            )
        )
    }
}