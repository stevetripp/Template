package com.example.template.ux.pullrefresh

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Clock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

@HiltViewModel
class PullRefreshViewModel
@Inject constructor() : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {
    private val random = Random(Clock.System.now().nanosecondsOfSecond)

    private val refreshFlow = MutableStateFlow<Int>(0)

    private val listItemsFlow = refreshFlow.mapLatest {
        isRefreshingFlow.value = true
        Log.i("SMT", "Is refreshing ...")
        delay(3000)
        isRefreshingFlow.value = false
        (1..100).map { index -> "$index: Item: ${random.nextInt()}" }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L), initialValue = emptyList())

    private val isRefreshingFlow = MutableStateFlow(false)

    val uiState = PullRefreshUiState(
        listItemsFlow = listItemsFlow,
        isRefreshingFlow = isRefreshingFlow,
        onRefresh = ::onRefresh,
    )

    private fun onRefresh() {
        refreshFlow.value = refreshFlow.value + 1
    }
}