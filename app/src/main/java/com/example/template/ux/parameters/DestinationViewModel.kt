package com.example.template.ux.parameters

import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import kotlinx.coroutines.flow.MutableStateFlow

class DestinationViewModel(
    destinationRoute: DestinationRoute
) : ViewModel() {
    private val reqParam1Flow = MutableStateFlow(destinationRoute.reqParam1)
    private val reqParam2Flow = MutableStateFlow(destinationRoute.reqParam2)
    private val optParam1Flow = MutableStateFlow(destinationRoute.optParam1)
    private val optParam2Flow = MutableStateFlow(destinationRoute.optParam2)

    init {
        SmtLogger.i("""destinationRoute: $destinationRoute""")
    }

    val uiState = DestinationUiState(
        reqParam1Flow = reqParam1Flow,
        reqParam2Flow = reqParam2Flow,
        optParam1Flow = optParam1Flow,
        optParam2Flow = optParam2Flow,
        onCloseBack = destinationRoute.closeOnBack
    )
}