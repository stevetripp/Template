package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ui.navigation.ViewModelNav
import com.example.template.ui.navigation.ViewModelNavImpl
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.flippable.FlippableRoute
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.pager.PagerRoute
import com.example.template.ux.panningzooming.PanningZoomingRoute
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.sidedrawer.SideDrawerRoute
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.swiperefresh.SwipeRefreshRoute
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.webview.WebViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    val uiState = HomeScreenUiState(
        onItemClicked = ::onItemClicked
    )

    private fun onItemClicked(screen: Screen) {
        when (screen) {
            Screen.HOME -> TODO()
            Screen.ANIMATED_GESTURE -> navigate(AnimatedGesturesRoute.routeDefinition)
            Screen.BOTTOM_NAVIGATION -> navigate(BottomNavigationRoute.routeDefinition)
            Screen.BOTTOM_SHEET -> navigate(BottomSheetRoute.routeDefinition)
            Screen.FLIPPABLE -> navigate(FlippableRoute.routeDefinition)
            Screen.IMAGE_PICKER -> navigate(ImagePickerRoute.routeDefinition)
            Screen.INPUT_EXAMPLES -> navigate(InputExamplesRoute.routeDefinition)
            Screen.MODAL_BOTTOM_SHEET -> navigate(ModalBottomSheetRoute.routeDefinition)
            Screen.NAVIGATION_PAGER -> navigate(NavigationPagerRoute.routeDefinition)
            Screen.PAGER -> navigate(PagerRoute.routeDefinition)
            Screen.PANNING_ZOOMING -> navigate(PanningZoomingRoute.routeDefinition)
            Screen.REORDERABLE_LIST -> navigate(ReorderableListRoute.routeDefinition)
            Screen.SIDE_DRAWER -> navigate(SideDrawerRoute.routeDefinition)
            Screen.SNACKBAR -> navigate(SnackbarRoute.routeDefinition)
            Screen.SWIPABLE -> navigate(SwipableRoute.routeDefinition)
            Screen.SWIPE_REFRESH -> navigate(SwipeRefreshRoute.routeDefinition)
            Screen.SYSTEM_UI -> navigate(SystemUiRoute.routeDefinition)
            Screen.TABS -> navigate(TabsRoute.routeDefinition)
            Screen.WEBVIEW -> navigate(WebViewRoute.routeDefinition)
        }
    }
}