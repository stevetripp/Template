package com.example.template.ux.popwithresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.mapNotNull
import org.lds.mobile.ext.stateInDefault
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl
import org.lds.mobile.navigation3.navigator.ResultStore

@HiltViewModel
class PopWithResultParentViewModel
@Inject
constructor(
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    val resultStringFlow = ResultStore.getResultFlow<String>(PopWithResultChildViewModel.CHILD_RESULT_KEY).mapNotNull {
        it?.let { ResultStore.removeResult<String>(PopWithResultChildViewModel.CHILD_RESULT_KEY) }
    }

    val uiState = PopWithResultParentUiState(
        resultStringFlow = resultStringFlow.stateInDefault(viewModelScope, ""),
        onClickMeClicked = { navigate(PopWithResultChildRoute) }
    )
}