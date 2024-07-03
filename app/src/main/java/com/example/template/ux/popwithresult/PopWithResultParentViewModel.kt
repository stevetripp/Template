package com.example.template.ux.popwithresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class PopWithResultParentViewModel
@Inject
constructor(
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val navControllerFlow = MutableStateFlow<NavController?>(null)
    private val currentBackStackEntryFlow = navControllerFlow.filterNotNull().flatMapLatest { it.currentBackStackEntryFlow }
    private val resultStringFlow = currentBackStackEntryFlow.flatMapLatest {
        it.savedStateHandle.getStateFlow(PopWithResultChildRoute.Arg.RESULT_STRING, "")
    }.stateInDefault(viewModelScope, null)

    val uiState = PopWithResultParentUiState(
        resultStringFlow = resultStringFlow,
        onClickMeClicked = { navigate(PopWithResultChildRoute) },
        onSetNavController = { navControllerFlow.value = it }
    )
}