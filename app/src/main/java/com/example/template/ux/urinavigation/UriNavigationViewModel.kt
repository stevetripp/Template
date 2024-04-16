package com.example.template.ux.urinavigation

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.template.ux.DeepLink
import com.example.template.ux.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import org.lds.mobile.navigation.ViewModelNav
import org.lds.mobile.navigation.ViewModelNavImpl
import javax.inject.Inject

data class UriNavigationUiState(
    val onDestination: () -> Unit = {},
    val onFlippable: () -> Unit = {},
    val onPullRefresh: () -> Unit = {},
)

@HiltViewModel
class UriNavigationViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl() {

    val uiState = UriNavigationUiState(
        onDestination = { navigate(Uri.parse("${DeepLink.ROOT}/${Screen.PARAMETERS.name}/Required_Path?OPTIONAL=Optional_Parameter")) },
        onFlippable = { navigate(Uri.parse("${DeepLink.ROOT}/${Screen.FLIPPABLE.name}")) },
        onPullRefresh = { navigate(Uri.parse("${DeepLink.ROOT}/${Screen.PULL_REFRESH.name}")) }
    )
}