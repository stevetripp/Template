package com.example.template.ux.home

import androidx.lifecycle.ViewModel
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.buttongroups.ButtonGroupsRoute
import com.example.template.ux.carousel.CarouselRoute
import com.example.template.ux.childwithnavigation.ChildWithNavigationRoute
import com.example.template.ux.chipsheet.ChipSheetRoute
import com.example.template.ux.datetimeformat.DateTimeFormatRoute
import com.example.template.ux.dialog.DialogRoute
import com.example.template.ux.flippable.FlippableRoute
import com.example.template.ux.gmailaddressfield.GmailAddressFieldRoute
import com.example.template.ux.imagepicker.ImagePickerRoute
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.main.Screen
import com.example.template.ux.main.ScreenType
import com.example.template.ux.memorize.MemorizeRoute
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.modalsidesheet.ModalSideSheetRoute
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
import com.example.template.ux.video.screen.VideoScreenRoute
import com.example.template.ux.webview.WebViewRoute
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

    private fun onItemClicked(screen: Screen) {
        when (screen) {
            Screen.ANIMATED_GESTURE -> navigate(AnimatedGesturesRoute)
            Screen.BOTTOM_NAVIGATION -> navigate(BottomNavigationRoute)
            Screen.BOTTOM_SHEET -> navigate(BottomSheetRoute)
            Screen.BUTTON_GROUPS -> navigate(ButtonGroupsRoute)
            Screen.CAROUSEL -> navigate(CarouselRoute)
            Screen.CHILD_WITH_NAVIGATION -> navigate(ChildWithNavigationRoute)
            Screen.CHIP_SHEET -> navigate(ChipSheetRoute)
            Screen.DATE_TIME_FORMAT -> navigate(DateTimeFormatRoute)
            Screen.DIALOG -> navigate(DialogRoute)
            Screen.FLIPPABLE -> navigate(FlippableRoute)
            Screen.GMAIL_ADDRESS_FIELD -> navigate(GmailAddressFieldRoute)
            Screen.IMAGE_PICKER -> navigate(ImagePickerRoute)
            Screen.INPUT_EXAMPLES -> navigate(InputExamplesRoute)
            Screen.MEMORIZE -> navigate(MemorizeRoute)
            Screen.MODAL_BOTTOM_SHEET -> navigate(ModalBottomSheetRoute)
            Screen.MODAL_SIDE_SHEET -> navigate(ModalSideSheetRoute)
            Screen.NAVIGATION_PAGER -> navigate(NavigationPagerRoute)
            Screen.NOTIFICATION_PERMISSIONS -> navigate(NotificationPermissionsRoute)
            Screen.PAGER -> navigate(PagerRoute)
            Screen.PANNING_ZOOMING -> navigate(PanningZoomingRoute)
            Screen.PARAMETERS -> navigate(ParametersRoute)
            Screen.PERMISSIONS -> navigate(PermissionsRoute)
            Screen.POP_WITH_RESULT -> navigate(PopWithResultParentRoute)
            Screen.PULL_REFRESH -> navigate(PullRefreshRoute)
            Screen.REORDERABLE_LIST -> navigate(ReorderableListRoute)
            Screen.SEARCH -> navigate(SearchRoute)
            Screen.SIDE_DRAWER -> navigate(SideDrawerRoute)
            Screen.SNACKBAR -> navigate(SnackbarRoute)
            Screen.STICKY_HEADERS -> navigate(StickyHeadersRoute)
            Screen.SWIPABLE -> navigate(SwipableRoute)
            Screen.SYSTEM_UI -> navigate(SystemUiRoute)
            Screen.TABS -> navigate(TabsRoute)
            Screen.URI_NAVIGATION -> navigate(UriNavigationRoute)
            Screen.VIDEO -> navigate(VideoScreenRoute)
            Screen.WEBVIEW -> navigate(WebViewRoute)
            else -> Unit
        }
    }
}