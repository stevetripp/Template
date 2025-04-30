package com.example.template.ux.parameters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.template.util.SmtLogger
import com.example.template.ux.NavTypeMaps
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val destinationRoute = savedStateHandle.toRoute<DestinationRoute>(NavTypeMaps.typeMap)
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
    )
}