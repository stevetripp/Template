package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation.ViewModelNavigation
import org.lds.mobile.navigation.ViewModelNavigationImpl
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImpl() {

    private val screens = Screen.entries.filter { it.type == ScreenType.UI }
    private val screensFlow = MutableStateFlow(screens)

    val uiState = HomeScreenUiState(
        screensFlow = screensFlow,
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) = navigate(screen.route)
}