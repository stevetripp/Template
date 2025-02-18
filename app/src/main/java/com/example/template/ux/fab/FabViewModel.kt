package com.example.template.ux.fab

import androidx.lifecycle.ViewModel
import com.example.template.model.data.SelectedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FabViewModel
@Inject constructor(
) : ViewModel() {

    private val fabTypesFlow = MutableStateFlow<List<SelectedObject<FabType>>>(FabType.entries.map { SelectedObject(it, it == FabType.DEFAULT) })

    val uiState = FabScreenUiState(
        fabTypesFlow = fabTypesFlow,
        onFabTypeChanged = ::onFabTypeChanged,
    )

    fun onFabTypeChanged(fabType: FabType) {
        fabTypesFlow.update { fabTypes -> fabTypes.map { it.copy(isSelected = it.obj == fabType) } }
    }
}