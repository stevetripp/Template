package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation.DefaultNavBarConfig
import org.lds.mobile.navigation.ViewModelNavBar
import org.lds.mobile.navigation.ViewModelNavBarImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
) : ViewModel(), ViewModelNavBar<NavBarItem> by ViewModelNavBarImpl(NavBarItem.UI_EXAMPLES, DefaultNavBarConfig(NavBarItem.getNavBarItemRouteMap()))