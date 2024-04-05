package com.example.template.ux.popwithresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.template.util.SmtLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class PopWithResultParentViewModel
@Inject
constructor(
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val navControllerFlow = MutableStateFlow<NavController?>(null)

    private val resultStringFlow = navControllerFlow.filterNotNull().flatMapLatest {
        SmtLogger.i("resultStringFlow")
        it.currentBackStackEntry?.savedStateHandle?.getStateFlow(PopWithResultChildRoute.Arg.RESULT_STRING, "") ?: flowOf(null)
    }.stateInDefault(viewModelScope, null)

    val uiState = PopWithResultParentUiState(
        resultStringFlow = resultStringFlow,
        onClickMeClicked = { navigate(PopWithResultChildRoute.createRoute()) },
        onSetNavController = { navControllerFlow.value = it }
    )
}