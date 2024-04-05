package com.example.template.ux.popwithresult

import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.PopResultKeyValue
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class PopWithResultChildViewModel
@Inject
constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val valueFlow = MutableStateFlow("")

    val uiState = PopWithResultChildUiState(
        valueFlow = valueFlow,
        onValueChanged = { valueFlow.value = it },
        onPopBackStack = {
            SmtLogger.i("onPopBackStack")
            popBackStackWithResult(listOf(PopResultKeyValue(PopWithResultChildRoute.Arg.RESULT_STRING, valueFlow.value)))
        }
    )
}