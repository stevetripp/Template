package com.example.template.ux.nesteddeeplink

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class Level2ViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val args = Level2Route.getArgs(savedStateHandle)

    init {
        viewModelScope.launch {
            delay(1000)
            args.deepLinkRoute?.let { navigate(Level3Route.createRoute()) }
        }
    }
}