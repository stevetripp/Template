package com.example.template.ux.modalsidesheet

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ModalSideSheetViewModel
@Inject constructor() : ViewModel() {

    private val isOpenFlow = MutableStateFlow(false)

    val uiState = ModalSideSheetUiState(
        isExpandedFlow = isOpenFlow,
        onExpandClicked = { isOpenFlow.value = true },
        onCloseClicked = {isOpenFlow.value =false}
    )
}