package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import org.lds.mobile.navigation3.ViewModelNavigation3
import org.lds.mobile.navigation3.ViewModelNavigation3Impl

@HiltViewModel
class HomeViewModel
@Inject constructor(
) : ViewModel(), ViewModelNavigation3 by ViewModelNavigation3Impl() {

    private val screens = Screen.entries.filter { it.type == ScreenType.UI }
    private val screensFlow = MutableStateFlow(screens)

    val uiState = HomeScreenUiState(
        screensFlow = screensFlow,
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) = navigate(screen.route)
}