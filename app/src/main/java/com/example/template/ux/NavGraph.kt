package com.example.template.ux

import androidx.compose.runtime.Composable
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.template.ux.DeepLink.HOST
import com.example.template.ux.DeepLink.PATH_PREFIX
import com.example.template.ux.DeepLink.SCHEME
import com.example.template.ux.about.AboutRoute
import com.example.template.ux.about.AboutScreen
import com.example.template.ux.animatedgestures.AnimatedGestureScreen
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomSheet.BottomSheetScreen
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.bottomnavigation.BottomNavigationScreen
import com.example.template.ux.carousel.CarouselRoute
import com.example.template.ux.carousel.CarouselScreen
import com.example.template.ux.childwithnavigation.ChildWithNavigationRoute
import com.example.template.ux.childwithnavigation.ChildWithNavigationScreen
import com.example.template.ux.childwithnavigation.ChildWithoutNavigationRoute
import com.example.template.ux.childwithnavigation.ChildWithoutNavigationScreen
import com.example.template.ux.chipsheet.ChipSheetRoute
import com.example.template.ux.chipsheet.ChipSheetScreen
import com.example.template.ux.datetimeformat.DateTimeFormatRoute
import com.example.template.ux.datetimeformat.DateTimeFormatScreen
import com.example.template.ux.dialog.DialogRoute
import com.example.template.ux.dialog.DialogScreen
import com.example.template.ux.flippable.FlippableRoute
import com.example.template.ux.flippable.FlippableScreen
import com.example.template.ux.gmailaddressfield.GmailAddressFieldRoute
import com.example.template.ux.gmailaddressfield.GmailAddressFieldScreen
import com.example.template.ux.home.HomeRoute
import com.example.template.ux.home.HomeScreen
import com.example.template.ux.imagepicker.ImagePickerRoute
import com.example.template.ux.imagepicker.ImagePickerScreen
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.inputexamples.InputExamplesScreen
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.modalbottomsheet.ModalBottomSheetScreen
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.navigatepager.NavigationPagerScreen
import com.example.template.ux.notificationpermissions.NotificationPermissionsRoute
import com.example.template.ux.notificationpermissions.NotificationPermissionsScreen
import com.example.template.ux.pager.PagerRoute
import com.example.template.ux.pager.PagerScreen
import com.example.template.ux.panningzooming.PanningZoomingRoute
import com.example.template.ux.panningzooming.PanningZoomingScreen
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.parameters.DestinationScreen
import com.example.template.ux.parameters.ParametersRoute
import com.example.template.ux.parameters.ParametersScreen
import com.example.template.ux.permissions.PermissionsRoute
import com.example.template.ux.permissions.PermissionsScreen
import com.example.template.ux.popwithresult.PopWithResultChildRoute
import com.example.template.ux.popwithresult.PopWithResultChildScreen
import com.example.template.ux.popwithresult.PopWithResultParentRoute
import com.example.template.ux.popwithresult.PopWithResultParentScreen
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.pullrefresh.PullRefreshScreen
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.reorderablelist.ReorderableListScreen
import com.example.template.ux.search.SearchRoute
import com.example.template.ux.search.SearchScreen
import com.example.template.ux.sidedrawer.SideDrawerRoute
import com.example.template.ux.sidedrawer.SideDrawerScreen
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.snackbar.SnackbarScreen
import com.example.template.ux.stickyheaders.StickyHeadersRoute
import com.example.template.ux.stickyheaders.StickyHeadersScreen
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.swipablescreen.SwipableScreen
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.systemui.SystemUiScreen
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.tabs.TabsScreen
import com.example.template.ux.urinavigation.UriNavigationRoute
import com.example.template.ux.urinavigation.UriNavigationScreen
import com.example.template.ux.webview.WebViewRoute
import com.example.template.ux.webview.WebViewScreen
import org.lds.mobile.navigation.NavUriLogger
import org.lds.mobile.ui.compose.navigation.NavComposeRoute

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Debug navigation
    navController.addOnDestinationChangedListener(NavUriLogger())

    NavHost(
        navController = navController,
        startDestination = HomeRoute.routeDefinition.value
    ) {
        AboutRoute.addNavigationRoute(this) { AboutScreen(navController) }
        DestinationRoute.addNavigationRoute(this) { DestinationScreen(navController) } // Putting first fixes crash
        HomeRoute.addNavigationRoute(this) { HomeScreen(navController) }
        AnimatedGesturesRoute.addNavigationRoute(this) { AnimatedGestureScreen(navController) }
        BottomNavigationRoute.addNavigationRoute(this) { BottomNavigationScreen(navController) }
        BottomSheetRoute.addNavigationRoute(this) { BottomSheetScreen(navController) }
        CarouselRoute.addNavigationRoute(this) { CarouselScreen(navController) }
        ChildWithNavigationRoute.addNavigationRoute(this) { ChildWithNavigationScreen(navController) }
        ChildWithoutNavigationRoute.addNavigationRoute(this) { ChildWithoutNavigationScreen(navController) }
        ChipSheetRoute.addNavigationRoute(this) { ChipSheetScreen(navController) }
        DateTimeFormatRoute.addNavigationRoute(this) { DateTimeFormatScreen(navController) }
        DialogRoute.addNavigationRoute(this) { DialogScreen(navController) }
        FlippableRoute.addNavigationRoute(this) { FlippableScreen(navController) }
        GmailAddressFieldRoute.addNavigationRoute(this) { GmailAddressFieldScreen(navController) }
        ImagePickerRoute.addNavigationRoute(this) { ImagePickerScreen(navController) }
        InputExamplesRoute.addNavigationRoute(this) { InputExamplesScreen(navController) }
        ModalBottomSheetRoute.addNavigationRoute(this) { ModalBottomSheetScreen(navController) }
        NavigationPagerRoute.addNavigationRoute(this) { NavigationPagerScreen(navController) }
        NotificationPermissionsRoute.addNavigationRoute(this) { NotificationPermissionsScreen(navController) }
        PagerRoute.addNavigationRoute(this) { PagerScreen(navController) }
        PanningZoomingRoute.addNavigationRoute(this) { PanningZoomingScreen(navController) }
        ParametersRoute.addNavigationRoute(this) { ParametersScreen(navController) }
        PermissionsRoute.addNavigationRoute(this) { PermissionsScreen(navController) }
        PopWithResultParentRoute.addNavigationRoute(this) { PopWithResultParentScreen(navController) }
        PopWithResultChildRoute.addNavigationRoute(this) { PopWithResultChildScreen(navController) }
        ReorderableListRoute.addNavigationRoute(this) { ReorderableListScreen(navController) }
        SearchRoute.addNavigationRoute(this) { SearchScreen(navController) }
        SideDrawerRoute.addNavigationRoute(this) { SideDrawerScreen(navController) }
        SnackbarRoute.addNavigationRoute(this) { SnackbarScreen(navController) }
        StickyHeadersRoute.addNavigationRoute(this) { StickyHeadersScreen(navController) }
        SwipableRoute.addNavigationRoute(this) { SwipableScreen(navController) }
        PullRefreshRoute.addNavigationRoute(this) { PullRefreshScreen(navController) }
        SystemUiRoute.addNavigationRoute(this) { SystemUiScreen(navController) }
        TabsRoute.addNavigationRoute(this) { TabsScreen(navController) }
        UriNavigationRoute.addNavigationRoute(this) { UriNavigationScreen(navController) }
        WebViewRoute.addNavigationRoute(this) { WebViewScreen(navController) }
    }
}

/**
 * Constants for the [NavDeepLink] URIs returned by the [NavComposeRoute]
 *
 * The [SCHEME], [HOST], and [PATH_PREFIX] should match the manifest intent-filter data elements respectively
 */
object DeepLink {
    private const val SCHEME = "https"
    private const val HOST = "my.website.org"
    private const val PATH_PREFIX = "/deeplink"

    const val ROOT = "$SCHEME://$HOST$PATH_PREFIX"
//    const val ROOT = "$SCHEME://$HOST"
}