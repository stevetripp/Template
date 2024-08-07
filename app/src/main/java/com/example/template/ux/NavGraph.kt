package com.example.template.ux

import androidx.compose.runtime.Composable
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.template.ux.buttongroups.ButtonGroupsRoute
import com.example.template.ux.buttongroups.ButtonGroupsScreen
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
import com.example.template.ux.flippable.deepLinks
import com.example.template.ux.gmailaddressfield.GmailAddressFieldRoute
import com.example.template.ux.gmailaddressfield.GmailAddressFieldScreen
import com.example.template.ux.home.HomeRoute
import com.example.template.ux.home.HomeScreen
import com.example.template.ux.imagepicker.ImagePickerRoute
import com.example.template.ux.imagepicker.ImagePickerScreen
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.inputexamples.InputExamplesScreen
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.ktor.KtorScreen
import com.example.template.ux.memorize.MemorizeRoute
import com.example.template.ux.memorize.MemorizeScreen
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.modalbottomsheet.ModalBottomSheetScreen
import com.example.template.ux.modalsidesheet.ModalSideSheetRoute
import com.example.template.ux.modalsidesheet.ModalSideSheetScreen
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
import com.example.template.ux.parameters.deepLinks
import com.example.template.ux.parameters.typeMap
import com.example.template.ux.permissions.PermissionsRoute
import com.example.template.ux.permissions.PermissionsScreen
import com.example.template.ux.popwithresult.PopWithResultChildRoute
import com.example.template.ux.popwithresult.PopWithResultChildScreen
import com.example.template.ux.popwithresult.PopWithResultParentRoute
import com.example.template.ux.popwithresult.PopWithResultParentScreen
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.example.template.ux.pullrefresh.PullRefreshScreen
import com.example.template.ux.pullrefresh.deepLinks
import com.example.template.ux.regex.RegexRoute
import com.example.template.ux.regex.RegexScreen
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.reorderablelist.ReorderableListScreen
import com.example.template.ux.search.SearchRoute
import com.example.template.ux.search.SearchScreen
import com.example.template.ux.servicesexamples.ServicesExamplesRoute
import com.example.template.ux.servicesexamples.ServicesExamplesScreen
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
import com.example.template.ux.video.player.PlayerActivity
import com.example.template.ux.video.player.PlayerRoute
import com.example.template.ux.video.player.typeMap
import com.example.template.ux.video.screen.VideoScreen
import com.example.template.ux.video.screen.VideoScreenRoute
import com.example.template.ux.webview.WebViewRoute
import com.example.template.ux.webview.WebViewScreen
import org.lds.mobile.navigation.NavUriLogger

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Debug navigation
    navController.addOnDestinationChangedListener(NavUriLogger())

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<AboutRoute> { AboutScreen(navController) }
        composable<AnimatedGesturesRoute> { AnimatedGestureScreen(navController) }
        composable<BottomNavigationRoute> { BottomNavigationScreen(navController) }
        composable<BottomSheetRoute> { BottomSheetScreen(navController) }
        composable<ButtonGroupsRoute> { ButtonGroupsScreen(navController) }
        composable<CarouselRoute> { CarouselScreen(navController) }
        composable<ChildWithNavigationRoute> { ChildWithNavigationScreen(navController) }
        composable<ChildWithoutNavigationRoute> { ChildWithoutNavigationScreen(navController) }
        composable<ChipSheetRoute> { ChipSheetScreen(navController) }
        composable<DateTimeFormatRoute> { DateTimeFormatScreen(navController) }
        composable<DestinationRoute>(typeMap = DestinationRoute.typeMap(), deepLinks = DestinationRoute.deepLinks()) { DestinationScreen(navController) }
        composable<DialogRoute> { DialogScreen(navController) }
        composable<FlippableRoute>(deepLinks = FlippableRoute.deepLinks()) { FlippableScreen(navController) }
        composable<GmailAddressFieldRoute> { GmailAddressFieldScreen(navController) }
        composable<HomeRoute> { HomeScreen(navController) }
        composable<ImagePickerRoute> { ImagePickerScreen(navController) }
        composable<InputExamplesRoute> { InputExamplesScreen(navController) }
        composable<KtorRoute> { KtorScreen(navController) }
        composable<MemorizeRoute> { MemorizeScreen(navController) }
        composable<ModalBottomSheetRoute> { ModalBottomSheetScreen(navController) }
        composable<ModalSideSheetRoute> { ModalSideSheetScreen(navController) }
        composable<NavigationPagerRoute> { NavigationPagerScreen(navController) }
        composable<NotificationPermissionsRoute> { NotificationPermissionsScreen(navController) }
        composable<PagerRoute> { PagerScreen(navController) }
        composable<PanningZoomingRoute> { PanningZoomingScreen(navController) }
        composable<ParametersRoute> { ParametersScreen(navController) }
        composable<PermissionsRoute> { PermissionsScreen(navController) }
        activity<PlayerRoute>(PlayerRoute.typeMap()) { this.activityClass = PlayerActivity::class }
        composable<PopWithResultChildRoute> { PopWithResultChildScreen(navController) }
        composable<PopWithResultParentRoute> { PopWithResultParentScreen(navController) }
        composable<PullRefreshRoute>(deepLinks = PullRefreshRoute.deepLinks()) { PullRefreshScreen(navController) }
        composable<RegexRoute> { RegexScreen(navController) }
        composable<ReorderableListRoute> { ReorderableListScreen(navController) }
        composable<SearchRoute> { SearchScreen(navController) }
        composable<ServicesExamplesRoute> { ServicesExamplesScreen(navController) }
        composable<SideDrawerRoute> { SideDrawerScreen(navController) }
        composable<SnackbarRoute> { SnackbarScreen(navController) }
        composable<StickyHeadersRoute> { StickyHeadersScreen(navController) }
        composable<SwipableRoute> { SwipableScreen(navController) }
        composable<SystemUiRoute> { SystemUiScreen(navController) }
        composable<TabsRoute> { TabsScreen(navController) }
        composable<UriNavigationRoute> { UriNavigationScreen(navController) }
        composable<VideoScreenRoute> { VideoScreen(navController) }
        composable<WebViewRoute> { WebViewScreen(navController) }
    }
}

/**
 * Constants for the [NavDeepLink] URIs
 *
 * The [SCHEME], [HOST], and [PATH_PREFIX] should match the manifest intent-filter data elements respectively
 */
object DeepLink {
    private const val SCHEME = "https"
    private const val HOST = "trippntechnology.com"
    private const val PATH_PREFIX = "/template"

    const val ROOT = "$SCHEME://$HOST$PATH_PREFIX"
}