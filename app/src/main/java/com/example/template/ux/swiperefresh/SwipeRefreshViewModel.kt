package com.example.template.ux.swiperefresh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SwipeRefreshViewModel
@Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    private val now = LocalDateTime.now()
    private val random = Random(LocalDateTime.now().nano)

    private val refreshFlow = MutableStateFlow<Int>(0)

    private val listItemsFlow = refreshFlow.mapLatest {
        isRefreshingFlow.value = true
        delay(3000)
        isRefreshingFlow.value = false
        (1..100).map { "$it: Item: ${random.nextInt()}" }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L), initialValue = emptyList())

    private val isRefreshingFlow = MutableStateFlow(false)

    val uiState = SwipeRefreshUiState(
        listItemsFlow = listItemsFlow,
        isRefreshingFlow = isRefreshingFlow,
        onRefresh = ::onRefresh,
    )

    private fun onRefresh() {
        refreshFlow.value = refreshFlow.value + 1
    }
}