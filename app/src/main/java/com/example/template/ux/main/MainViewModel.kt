package com.example.template.ux.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation.DefaultNavigationBarConfig
import org.lds.mobile.navigation.ViewModelNavigationBar
import org.lds.mobile.navigation.ViewModelNavigationBarImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
) : ViewModel(), ViewModelNavigationBar<NavBarItem> by ViewModelNavigationBarImpl(NavBarItem.UI_EXAMPLES, DefaultNavigationBarConfig(NavBarItem.getNavBarItemRouteMap()))