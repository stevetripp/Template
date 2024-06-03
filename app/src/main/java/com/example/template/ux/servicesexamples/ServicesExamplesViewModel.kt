package com.example.template.ux.servicesexamples

import androidx.lifecycle.ViewModel
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class ServicesExamplesViewModel
@Inject constructor(
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    private val screens = Screen.entries.filter { it.type == ScreenType.SERVICES }
    private val screensFlow = MutableStateFlow(screens)

    val uiState = ServicesExamplesScreenUiState(
        screensFlow = screensFlow,
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) {
        when (screen) {
            Screen.KTOR -> navigate(KtorRoute.createRoute())
            else -> Unit
        }
    }
}