package com.example.template.ux.popwithresult

import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl
import org.lds.mobile.navigation3.navigator.ResultKey
import org.lds.mobile.navigation3.navigator.ResultStore

class PopWithResultChildViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val valueFlow = MutableStateFlow("")

    val uiState = PopWithResultChildUiState(
        valueFlow = valueFlow,
        onValueChanged = { valueFlow.value = it },
        onPopBackStack = {
            SmtLogger.i("onPopBackStack")
            ResultStore.setResult(CHILD_RESULT_KEY, valueFlow.value)
            popBackStack()
        }
    )

    companion object {
        val CHILD_RESULT_KEY = object : ResultKey {}
    }
}