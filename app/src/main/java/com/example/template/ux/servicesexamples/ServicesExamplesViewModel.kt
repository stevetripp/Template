package com.example.template.ux.servicesexamples

import androidx.lifecycle.ViewModel
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import com.example.template.ux.regex.RegexRoute
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

class ServicesExamplesViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val screens = Screen.entries.filter { it.type == ScreenType.SERVICES }
    private val screensFlow = MutableStateFlow(screens)

    val uiState = ServicesExamplesScreenUiState(
        screensFlow = screensFlow,
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) {
        when (screen) {
            Screen.KTOR -> navigate(KtorRoute)
            Screen.REGEX -> navigate(RegexRoute)
            else -> Unit
        }
    }
}