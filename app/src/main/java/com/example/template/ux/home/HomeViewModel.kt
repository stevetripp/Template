package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.util.SmtLogger
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation.NavRoute
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    val uiState = HomeScreenUiState(
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) {
        val navRoute = NavRoute(screen.name)
        SmtLogger.i(navRoute.toString())
        navigate(navRoute)
    }
}