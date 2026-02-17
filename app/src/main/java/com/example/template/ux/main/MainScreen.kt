package com.example.template.ux.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.ui.NavDisplay
import com.example.template.util.SmtLogger
import com.example.template.ux.about.AboutRoute
import com.example.template.ux.about.AboutScreen
import com.example.template.ux.animatedgestures.AnimatedGestureScreen
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomSheet.BottomSheetScreen
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.bottomnavigation.BottomNavigationScreen
import com.example.template.ux.breadcrumbs.BreadcrumbRoute
import com.example.template.ux.breadcrumbs.BreadcrumbsRoute
import com.example.template.ux.breadcrumbs.BreadcrumbsScreen
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
import com.example.template.ux.deeplink.DeepLinkRoute
import com.example.template.ux.deeplink.DeepLinkScreen
import com.example.template.ux.dialog.DialogRoute
import com.example.template.ux.dialog.DialogScreen
import com.example.template.ux.edgetoedge.EdgeToEdgeRoute
import com.example.template.ux.edgetoedge.EdgeToEdgeScreen
import com.example.template.ux.fab.FabRoute
import com.example.template.ux.fab.FabScreen
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
import com.example.template.ux.ktor.KtorRoute
import com.example.template.ux.ktor.KtorScreen
import com.example.template.ux.memorize.MemorizeRoute
import com.example.template.ux.memorize.MemorizeScreen
import com.example.template.ux.modalbottomsheet.ModalBottomSheetRoute
import com.example.template.ux.modalbottomsheet.ModalBottomSheetScreen
import com.example.template.ux.modaldrawersheet.ModalDrawerSheetRoute
import com.example.template.ux.modaldrawersheet.ModelDrawerSheetScreen
import com.example.template.ux.modalsidesheet.ModalSideSheetRoute
import com.example.template.ux.modalsidesheet.ModalSideSheetScreen
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.navigatepager.NavigationPagerScreen
import com.example.template.ux.notification.NotificationRoute
import com.example.template.ux.notification.NotificationScreen
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
import com.example.template.ux.regex.RegexRoute
import com.example.template.ux.regex.RegexScreen
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.reorderablelist.ReorderableListScreen
import com.example.template.ux.search.SearchRoute
import com.example.template.ux.search.SearchScreen
import com.example.template.ux.servicesexamples.ServicesExamplesRoute
import com.example.template.ux.servicesexamples.ServicesExamplesScreen
import com.example.template.ux.settings.SettingRoute
import com.example.template.ux.settings.SettingsScreen
import com.example.template.ux.sidedrawer.SideDrawerRoute
import com.example.template.ux.sidedrawer.SideDrawerScreen
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.snackbar.SnackbarScreen
import com.example.template.ux.stickyheaders.StickyHeadersRoute
import com.example.template.ux.stickyheaders.StickyHeadersScreen
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.swipablescreen.SwipableScreen
import com.example.template.ux.synchronizescrolling.SynchronizeScrollingRoute
import com.example.template.ux.synchronizescrolling.SynchronizeScrollingScreen
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.systemui.SystemUiScreen
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.tabs.TabsScreen
import com.example.template.ux.typography.TypographyRoute
import com.example.template.ux.typography.TypographyScreen
import com.example.template.ux.video.player.PlayerActivity
import com.example.template.ux.video.player.PlayerRoute
import com.example.template.ux.video.screen.VideoScreen
import com.example.template.ux.video.screen.VideoScreenRoute
import com.example.template.ux.webview.WebViewRoute
import com.example.template.ux.webview.WebViewScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.lds.mobile.navigation3.NavigationState
import org.lds.mobile.navigation3.navigator.TopLevelBackStackNavigator
import org.lds.mobile.ui.compose.navigation.ObserveRouteChanges
import org.lds.mobile.ui.compose.navigation.rememberNavigationState
import org.lds.mobile.ui.compose.navigation.toEntries

@Composable
fun MainScreen(deeplinkRoute: NavKey?) {

    val navigationState: NavigationState = rememberNavigationState(
        startRoute = HomeRoute,
        topLevelRoutes = NavBarItem.entries.map { it.route }.toSet(),
        navKeySerializer = NavKeySerializer()
    )

    val navigator = TopLevelBackStackNavigator(navigationState)
    val backstack = navigator.getCurrentBackStack()
    val breadcrumbRoutes = backstack?.mapNotNull { it as? BreadcrumbRoute }.orEmpty()

    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<AboutRoute> { AboutScreen(navigator) }
        entry<AnimatedGesturesRoute> { AnimatedGestureScreen(navigator) }
        entry<BottomNavigationRoute> { BottomNavigationScreen(navigator) }
        entry<BottomSheetRoute> { BottomSheetScreen(navigator) }
        entry<BreadcrumbsRoute> { BreadcrumbsScreen(navigator = navigator, viewModel = koinViewModel { parametersOf(breadcrumbRoutes) }) }
        entry<ButtonGroupsRoute> { ButtonGroupsScreen(navigator) }
        entry<CarouselRoute> { CarouselScreen(navigator) }
        entry<ChildWithNavigationRoute> { ChildWithNavigationScreen(navigator) }
        entry<ChildWithoutNavigationRoute> { ChildWithoutNavigationScreen(navigator) }
        entry<ChipSheetRoute> { ChipSheetScreen(navigator, koinViewModel()) }
        entry<DateTimeFormatRoute> { DateTimeFormatScreen(navigator) }
        entry<DeepLinkRoute> { key -> DeepLinkScreen(navigator, key) }
        entry<DestinationRoute> { key -> DestinationScreen(navigator, koinViewModel { parametersOf(key) }) }
        entry<DialogRoute> { DialogScreen(navigator, koinViewModel()) }
        entry<EdgeToEdgeRoute> { EdgeToEdgeScreen(navigator, koinViewModel()) }
        entry<FabRoute> { FabScreen(navigator, koinViewModel()) }
        entry<FlippableRoute> { FlippableScreen(navigator) }
        entry<GmailAddressFieldRoute> { GmailAddressFieldScreen(navigator, koinViewModel()) }
        entry<HomeRoute> { HomeScreen(navigator, koinViewModel()) }
        entry<ImagePickerRoute> { ImagePickerScreen(navigator) }
        entry<InputExamplesRoute> { InputExamplesScreen(navigator) }
        entry<KtorRoute> { KtorScreen(navigator, koinViewModel()) }
        entry<MemorizeRoute> { MemorizeScreen(navigator, koinViewModel()) }
        entry<ModalBottomSheetRoute> { ModalBottomSheetScreen(navigator) }
        entry<ModalDrawerSheetRoute> { ModelDrawerSheetScreen(navigator) }
        entry<ModalSideSheetRoute> { ModalSideSheetScreen(navigator, koinViewModel()) }
        entry<NavigationPagerRoute> { NavigationPagerScreen(navigator) }
        entry<NotificationPermissionsRoute> { NotificationPermissionsScreen(navigator, koinViewModel()) }
        entry<NotificationRoute> { NotificationScreen(navigator, koinViewModel()) }
        entry<PagerRoute> { PagerScreen(navigator) }
        entry<PanningZoomingRoute> { PanningZoomingScreen(navigator) }
        entry<ParametersRoute> { ParametersScreen(navigator, koinViewModel()) }
        entry<PermissionsRoute> { PermissionsScreen(navigator, koinViewModel()) }
        entry<PopWithResultChildRoute> { PopWithResultChildScreen(navigator, koinViewModel()) }
        entry<PopWithResultParentRoute> { PopWithResultParentScreen(navigator, koinViewModel()) }
        entry<PullRefreshRoute> { key -> PullRefreshScreen(navigator, koinViewModel { parametersOf(key) }) }
        entry<RegexRoute> { RegexScreen(navigator, koinViewModel()) }
        entry<ReorderableListRoute> { ReorderableListScreen(navigator, koinViewModel()) }
        entry<SearchRoute> { SearchScreen(navigator, koinViewModel()) }
        entry<ServicesExamplesRoute> { ServicesExamplesScreen(navigator, koinViewModel()) }
        entry<SettingRoute> { SettingsScreen(navigator, koinViewModel()) }
        entry<SideDrawerRoute> { SideDrawerScreen(navigator) }
        entry<SnackbarRoute> { SnackbarScreen(navigator, koinViewModel()) }
        entry<StickyHeadersRoute> { StickyHeadersScreen(navigator, koinViewModel()) }
        entry<SwipableRoute> { SwipableScreen(navigator) }
        entry<SynchronizeScrollingRoute> { SynchronizeScrollingScreen(navigator, koinViewModel()) }
        entry<SystemUiRoute> { SystemUiScreen(navigator) }
        entry<TabsRoute> { TabsScreen(navigator) }
        entry<TypographyRoute> { TypographyScreen(navigator) }
        entry<VideoScreenRoute> { VideoScreen(navigator, koinViewModel()) }
        entry<WebViewRoute> { WebViewScreen(navigator) }
        entry<PlayerRoute> { key ->
            // NOTE: I prefer to launch the PlayerActivity here, but it caused the pop to result in a blank screen and required an
            // additional back.
            PlayerActivity.launch(LocalContext.current, key.videoId)
        }
    }

    val decorators: List<NavEntryDecorator<NavKey>> = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator())

    LaunchedEffect(deeplinkRoute) {
        // LaunchEffect used to ignore deeplinkRoute when MainScreen is recomposed and it hasn't changed
        deeplinkRoute?.let { navigator.navigate(it) }
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider, decorators),
        onBack = { navigator.pop() }
    )

    backstack?.let { ObserveRouteChanges(it) { navKey -> SmtLogger.i("""Navigating to: $navKey""") } }
}

