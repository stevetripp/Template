package com.example.template.ux.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
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
import com.example.template.ux.parameters.DestinationViewModel
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
import com.example.template.ux.pullrefresh.PullRefreshViewModel
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
import org.lds.mobile.navigation3.NavigationState
import org.lds.mobile.navigation3.navigator.TopLevelBackStackNavigator
import org.lds.mobile.ui.compose.navigation.rememberNavigationState
import org.lds.mobile.ui.compose.navigation.toEntries

@Composable
fun MainScreen(deeplinkRoute: NavKey?, mainViewModel: MainViewModel = hiltViewModel()) {

    val navigationState: NavigationState = rememberNavigationState(
        startRoute = HomeRoute,
        topLevelRoutes = NavBarItem.entries.map { it.route }.toSet(),
        navKeySerializer = NavKeySerializer()
    )

    val navigator: TopLevelBackStackNavigator = TopLevelBackStackNavigator(navigationState)
    val backstack = navigator.getCurrentBackStack()

    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<AboutRoute> { AboutScreen(navigator) }
        entry<AnimatedGesturesRoute> { AnimatedGestureScreen(navigator) }
        entry<BottomNavigationRoute> { BottomNavigationScreen(navigator) }
        entry<BottomSheetRoute> { BottomSheetScreen(navigator) }
        entry<BreadcrumbsRoute> { key ->
            val breadcrumbRoutes = backstack?.mapNotNull { it as? BreadcrumbRoute } ?: emptyList()
            BreadcrumbsScreen(navigator, breadcrumbRoutes, hiltViewModel())
        }
        entry<ButtonGroupsRoute> { ButtonGroupsScreen(navigator) }
        entry<CarouselRoute> { CarouselScreen(navigator) }
        entry<ChildWithNavigationRoute> { ChildWithNavigationScreen(navigator) }
        entry<ChildWithoutNavigationRoute> { ChildWithoutNavigationScreen(navigator) }
        entry<ChipSheetRoute> { ChipSheetScreen(navigator, hiltViewModel()) }
        entry<DateTimeFormatRoute> { DateTimeFormatScreen(navigator) }
        entry<DestinationRoute> { key -> DestinationScreen(navigator, hiltViewModel<DestinationViewModel, DestinationViewModel.Factory>(creationCallback = { it.create(key) })) }
        entry<DialogRoute> { DialogScreen(navigator, hiltViewModel()) }
        entry<EdgeToEdgeRoute> { EdgeToEdgeScreen(navigator, hiltViewModel()) }
        entry<FabRoute> { FabScreen(navigator, hiltViewModel()) }
        entry<FlippableRoute> { FlippableScreen(navigator) }
        entry<GmailAddressFieldRoute> { GmailAddressFieldScreen(navigator, hiltViewModel()) }
        entry<HomeRoute> { HomeScreen(navigator, hiltViewModel()) }
        entry<ImagePickerRoute> { ImagePickerScreen(navigator) }
        entry<InputExamplesRoute> { InputExamplesScreen(navigator) }
        entry<KtorRoute> { KtorScreen(navigator, hiltViewModel()) }
        entry<MemorizeRoute> { MemorizeScreen(navigator, hiltViewModel()) }
        entry<ModalBottomSheetRoute> { ModalBottomSheetScreen(navigator) }
        entry<ModalDrawerSheetRoute> { ModelDrawerSheetScreen(navigator) }
        entry<ModalSideSheetRoute> { ModalSideSheetScreen(navigator, hiltViewModel()) }
        entry<NavigationPagerRoute> { NavigationPagerScreen(navigator) }
        entry<NotificationPermissionsRoute> { NotificationPermissionsScreen(navigator, hiltViewModel()) }
        entry<NotificationRoute> { NotificationScreen(navigator, hiltViewModel()) }
        entry<PagerRoute> { PagerScreen(navigator) }
        entry<PanningZoomingRoute> { PanningZoomingScreen(navigator) }
        entry<ParametersRoute> { ParametersScreen(navigator, hiltViewModel()) }
        entry<PermissionsRoute> { PermissionsScreen(navigator, hiltViewModel()) }
        entry<PopWithResultChildRoute> { PopWithResultChildScreen(navigator, hiltViewModel()) }
        entry<PopWithResultParentRoute> { PopWithResultParentScreen(navigator) }
        entry<PullRefreshRoute> { key -> PullRefreshScreen(navigator, hiltViewModel<PullRefreshViewModel, PullRefreshViewModel.Factory>(creationCallback = { it.create(key) })) }
        entry<RegexRoute> { RegexScreen(navigator, hiltViewModel()) }
        entry<ReorderableListRoute> { ReorderableListScreen(navigator, hiltViewModel()) }
        entry<SearchRoute> { SearchScreen(navigator, hiltViewModel()) }
        entry<ServicesExamplesRoute> { ServicesExamplesScreen(navigator, hiltViewModel()) }
        entry<SettingRoute> { SettingsScreen(navigator, hiltViewModel()) }
        entry<SideDrawerRoute> { SideDrawerScreen(navigator) }
        entry<SnackbarRoute> { SnackbarScreen(navigator, hiltViewModel()) }
        entry<StickyHeadersRoute> { StickyHeadersScreen(navigator, hiltViewModel()) }
        entry<SwipableRoute> { SwipableScreen(navigator) }
        entry<SynchronizeScrollingRoute> { SynchronizeScrollingScreen(navigator, hiltViewModel()) }
        entry<SystemUiRoute> { SystemUiScreen(navigator) }
        entry<TabsRoute> { TabsScreen(navigator) }
        entry<TypographyRoute> { TypographyScreen(navigator) }
        entry<VideoScreenRoute> { VideoScreen(navigator, hiltViewModel()) }
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


/**
 * Observes changes to the navigation back stack and invokes a callback when the current route changes.
 *
 * This composable tracks the back stack state across recompositions and detects when the current
 * route (last item in the back stack) changes. When a change is detected, it invokes the
 * [onRouteChanged] callback with the new current route.
 *
 * @param currentBackStack The navigation back stack to observe
 * @param ignoreBack If true, skips notification when navigating backwards through the stack.
 *                   Only notifies on forward navigation. Defaults to false.
 * @param onRouteChanged Callback invoked with the current route when it changes
 */
@Composable
fun ObserveRouteChanges(currentBackStack: NavBackStack<NavKey>, ignoreBack: Boolean = false, onRouteChanged: (NavKey) -> Unit) {
    // Track the previous back stack state to detect navigation changes
    val previousBackStack = rememberNavBackStack()

    // Early exit if back stack size hasn't changed
    if (previousBackStack.size == currentBackStack.size) return

    // Determine if user is navigating backwards through the stack
    val navigatingBack = previousBackStack.size > currentBackStack.size

    // Update tracked state with current back stack
    previousBackStack.clear()
    previousBackStack.addAll(currentBackStack)

    // Skip logging if we should ignore back navigation
    if (ignoreBack && navigatingBack) return

    // Get the most recent route from the back stack
    val currentRoute = currentBackStack.last()

    // Notify via callback when the current route changes
    LaunchedEffect(currentRoute) {
        onRouteChanged(currentRoute)
    }
}
