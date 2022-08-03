package com.example.template.ux

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.template.ux.animatedgestures.AnimatedGestureScreen
import com.example.template.ux.animatedgestures.AnimatedGesturesRoute
import com.example.template.ux.bottomSheet.BottomSheetRoute
import com.example.template.ux.bottomSheet.BottomSheetScreen
import com.example.template.ux.bottomnavigation.BottomNavigationRoute
import com.example.template.ux.bottomnavigation.BottomNavigationScreen
import com.example.template.ux.home.HomeRoute
import com.example.template.ux.home.HomeScreen
import com.example.template.ux.inputexamples.InputExamplesRoute
import com.example.template.ux.inputexamples.InputExamplesScreen
import com.example.template.ux.navigatepager.NavigationPagerRoute
import com.example.template.ux.navigatepager.NavigationPagerScreen
import com.example.template.ux.pager.PagerRoute
import com.example.template.ux.pager.PagerScreen
import com.example.template.ux.panningzooming.PanningZoomingRoute
import com.example.template.ux.panningzooming.PanningZoomingScreen
import com.example.template.ux.reorderablelist.ReorderableListRoute
import com.example.template.ux.reorderablelist.ReorderableListScreen
import com.example.template.ux.snackbar.SnackbarRoute
import com.example.template.ux.snackbar.SnackbarScreen
import com.example.template.ux.swipablescreen.SwipableRoute
import com.example.template.ux.swipablescreen.SwipableScreen
import com.example.template.ux.systemui.SystemUiRoute
import com.example.template.ux.systemui.SystemUiScreen
import com.example.template.ux.tabs.TabsRoute
import com.example.template.ux.tabs.TabsScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Debug navigation
//    navController.addOnDestinationChangedListener(NavUriLogger())

    NavHost(
        navController = navController,
        startDestination = HomeRoute.routeDefinition
    ) {
        HomeRoute.addNavigationRoute(this) { HomeScreen(navController) }
        AnimatedGesturesRoute.addNavigationRoute(this) { AnimatedGestureScreen(navController) }
        BottomNavigationRoute.addNavigationRoute(this) { BottomNavigationScreen(navController) }
        BottomSheetRoute.addNavigationRoute(this) { BottomSheetScreen(navController) }
        InputExamplesRoute.addNavigationRoute(this) { InputExamplesScreen(navController) }
        NavigationPagerRoute.addNavigationRoute(this) { NavigationPagerScreen(navController) }
        PagerRoute.addNavigationRoute(this) { PagerScreen(navController) }
        PanningZoomingRoute.addNavigationRoute(this) { PanningZoomingScreen(navController) }
        ReorderableListRoute.addNavigationRoute(this) { ReorderableListScreen(navController) }
        SnackbarRoute.addNavigationRoute(this) { SnackbarScreen(navController) }
        SwipableRoute.addNavigationRoute(this) { SwipableScreen(navController) }
        SystemUiRoute.addNavigationRoute(this) { SystemUiScreen(navController) }
        TabsRoute.addNavigationRoute(this) { TabsScreen(navController) }
    }
}
