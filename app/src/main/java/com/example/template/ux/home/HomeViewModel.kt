package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.carousel.CarouselRoute
import com.example.template.ux.chipsheet.ChipSheetRoute
import com.example.template.ux.datetimeformat.DateTimeFormatRoute
import com.example.template.ux.dialog.DialogRoute
import com.example.template.ux.flippable.FlippableRoute
import com.example.template.ux.gmailaddressfield.GmailAddressFieldRoute
import com.example.template.ux.imagepicker.ImagePickerRoute
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.notificationpermissions.NotificationPermissionsRoute
import com.example.template.ux.pager.PagerRoute
import com.example.template.ux.panningzooming.PanningZoomingRoute
import com.example.template.ux.parameters.ParametersRoute
import com.example.template.ux.permissions.PermissionsRoute
import com.example.template.ux.popwithresult.PopWithResultParentRoute
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.search.SearchRoute
import com.example.template.ux.sidedrawer.SideDrawerRoute
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.stickyheaders.StickyHeadersRoute
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.urinavigation.UriNavigationRoute
import com.example.template.ux.webview.WebViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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
        when (screen) {
            Screen.HOME -> TODO()
            Screen.ANIMATED_GESTURE -> navigate(AnimatedGesturesRoute.createRoute())
            Screen.BOTTOM_NAVIGATION -> navigate(BottomNavigationRoute.createRoute())
            Screen.BOTTOM_SHEET -> navigate(BottomSheetRoute.createRoute())
            Screen.CAROUSEL -> navigate(CarouselRoute.createRoute())
            Screen.CHIP_SHEET -> navigate(ChipSheetRoute.createRoute())
            Screen.DATE_TIME_FORMAT -> navigate(DateTimeFormatRoute.createRoute())
            Screen.DIALOG -> navigate(DialogRoute.createRoute())
            Screen.FLIPPABLE -> navigate(FlippableRoute.createRoute())
            Screen.GMAIL_ADDRESS_FIELD -> navigate(GmailAddressFieldRoute.createRoute())
            Screen.IMAGE_PICKER -> navigate(ImagePickerRoute.createRoute())
            Screen.INPUT_EXAMPLES -> navigate(InputExamplesRoute.createRoute())
            Screen.MODAL_BOTTOM_SHEET -> navigate(ModalBottomSheetRoute.createRoute())
            Screen.NAVIGATION_PAGER -> navigate(NavigationPagerRoute.createRoute())
            Screen.NOTIFICATION_PERMISSIONS -> navigate(NotificationPermissionsRoute.createRoute())
            Screen.PAGER -> navigate(PagerRoute.createRoute())
            Screen.PANNING_ZOOMING -> navigate(PanningZoomingRoute.createRoute())
            Screen.PARAMETERS -> navigate(ParametersRoute.createRoute())
            Screen.PERMISSIONS -> navigate(PermissionsRoute.createRoute())
            Screen.POP_WITH_RESULT -> navigate(PopWithResultParentRoute.createRoute())
            Screen.PULL_REFRESH -> navigate(PullRefreshRoute.createRoute())
            Screen.REORDERABLE_LIST -> navigate(ReorderableListRoute.createRoute())
            Screen.SEARCH -> navigate(SearchRoute.createRoute())
            Screen.SIDE_DRAWER -> navigate(SideDrawerRoute.createRoute())
            Screen.SNACKBAR -> navigate(SnackbarRoute.createRoute())
            Screen.STICKY_HEADERS -> navigate(StickyHeadersRoute.createRoute())
            Screen.SWIPABLE -> navigate(SwipableRoute.createRoute())
            Screen.SYSTEM_UI -> navigate(SystemUiRoute.createRoute())
            Screen.TABS -> navigate(TabsRoute.createRoute())
            Screen.URI_NAVIGATION -> navigate(UriNavigationRoute.createRoute())
            Screen.WEBVIEW -> navigate(WebViewRoute.createRoute())
        }
    }
}