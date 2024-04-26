package com.example.template.ux.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor() : ViewModel() {

    private val _startupCompleteFlow = MutableStateFlow<Boolean>(false)
    val startupCompleteFlow: StateFlow<Boolean> = _startupCompleteFlow.asStateFlow()

    private val _countdownFlow = MutableStateFlow<Int>(COUNT_DOWN)
    val countdownFlow: StateFlow<Int> = _countdownFlow.asStateFlow()

    fun startup() = viewModelScope.launch {
        for (i in COUNT_DOWN downTo 1) {
            _countdownFlow.value = i
            delay(1000)
        }
        _startupCompleteFlow.compareAndSet(false, true)
    }

    companion object {
        private const val COUNT_DOWN = 3
    }
}