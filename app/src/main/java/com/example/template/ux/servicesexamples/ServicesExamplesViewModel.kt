package com.example.template.ux.servicesexamples

import androidx.lifecycle.ViewModel
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import com.example.template.ux.regex.RegexRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class ServicesExamplesViewModel
@Inject constructor(
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

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