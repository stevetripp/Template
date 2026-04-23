package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import com.example.template.ux.main.UiCategory
import com.example.template.ux.settings.SettingRoute
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

class HomeViewModel : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val inputScreens = Screen.entries.filter { it.type == ScreenType.UI && it.category == UiCategory.INPUT }
    private val navigationScreens = Screen.entries.filter { it.type == ScreenType.UI && it.category == UiCategory.NAVIGATION }
    private val visualScreens = Screen.entries.filter { it.type == ScreenType.UI && it.category == UiCategory.VISUAL }

    private val inputScreensFlow = MutableStateFlow(inputScreens)
    private val navigationScreensFlow = MutableStateFlow(navigationScreens)
    private val visualScreensFlow = MutableStateFlow(visualScreens)

    val uiState = HomeScreenUiState(
        inputScreensFlow = inputScreensFlow,
        navigationScreensFlow = navigationScreensFlow,
        visualScreensFlow = visualScreensFlow,
        onItemClicked = ::onItemClicked,
        onSettingsClicked = { navigate(SettingRoute) }
    )

    private fun onItemClicked(screen: Screen) = navigate(screen.route)
}