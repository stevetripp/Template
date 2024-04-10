package com.example.template.ux.nesteddeeplink

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.util.SmtLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class Level1ViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val args = Level1Route.getArgs(savedStateHandle)

    init {
        SmtLogger.i("Level1ViewModel")
        viewModelScope.launch {
            savedStateHandle[Level1Route.Arg.DEEP_LINK_ROUTE] = null
            delay(1000)
            args.deepLinkRoute?.let { navigate(Level2Route.createRoute(it)) }
        }
    }

    fun onLevelTwoClicked() = navigate(Level2Route.createRoute())
    fun onLevelThreeClicked() = navigate(Level3Route.createRoute())
}