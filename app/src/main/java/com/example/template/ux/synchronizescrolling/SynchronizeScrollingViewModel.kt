package com.example.template.ux.synchronizescrolling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ux.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import org.lds.mobile.ext.stateInDefault
import javax.inject.Inject

@HiltViewModel
class SynchronizeScrollingViewModel @Inject constructor() : ViewModel() {

    private val queryFlow = MutableStateFlow<String>("")
    private val randomNamesFlow = MutableStateFlow(SearchViewModel.randomNames)
    private val namesFlow = combine(queryFlow, randomNamesFlow) { query, names ->
        names.filter { it.contains(query, true) }
    }.stateInDefault(viewModelScope, emptyList())

    val uiState = SynchronizeScrollingUiState(
        namesFlow = namesFlow,
        queryFlow = queryFlow,
        onQueryChange = { queryFlow.value = it }
    )
}