package com.example.template.ux.modalsidesheet

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ModalSideSheetViewModel : ViewModel() {

    private val isOpenFlow = MutableStateFlow(false)

    val uiState = ModalSideSheetUiState(
        isExpandedFlow = isOpenFlow,
        onExpandClicked = { isOpenFlow.value = true },
        onCloseClicked = { isOpenFlow.value = false }
    )
}