package com.example.template.ux

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.template.ux.main.MainViewModel
import com.example.template.ux.main.NavBarItem
import org.lds.mobile.ui.compose.WindowSize
import org.lds.mobile.ui.compose.rememberWindowSize
import org.lds.mobile.ui.ext.requireActivity

@Composable
fun MainAppScaffoldWithNavBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIconVisible: Boolean = true,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onNavigationClick: (() -> Unit)? = null,
    hideNavigation: Boolean = false,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0), // required when using enableEdgeToEdge
    content: @Composable () -> Unit,
) {
    MainAppScaffoldWithNavBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIconVisible = navigationIconVisible,
        navigationIcon = navigationIcon,
        onNavigationClick = onNavigationClick,
        hideNavigation = hideNavigation,
        actions = actions,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}

@Composable
fun MainAppScaffoldWithNavBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIconVisible: Boolean = true,
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onNavigationClick: (() -> Unit)? = null,
    hideNavigation: Boolean = false,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0), // required when using enableEdgeToEdge
    content: @Composable () -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val viewModel: MainViewModel? = if (isPreview) null else hiltViewModel(LocalContext.current.requireActivity())
    val selectedNavBarItem = viewModel?.selectedNavBarFlow?.collectAsStateWithLifecycle()?.value
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior() // Set to TopAppBarDefaults.pinnedScrollBehavior() when not MediumTopAppBar
    val windowDpSize = if (isPreview) IntSize(0, 0) else currentWindowSize()

    // TopAppBar
    val topAppBar: @Composable (() -> Unit) = {
        MediumTopAppBar(
            title = title,
            navigationIcon = if (!navigationIconVisible) {
                {}
            } else {
                {
                    IconButton(
                        onClick = { onNavigationClick?.invoke() }) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = "Back",
                            modifier = Modifier
                        )
                    }
                }
            },
            actions = { actions?.invoke(this) },
            scrollBehavior = scrollBehavior
        )
    }

    NavigationSuiteScaffold(
        layoutType = if (hideNavigation) NavigationSuiteType.None else getNavigationSuiteType(windowDpSize.toDpSize()),
        navigationSuiteItems = {
            NavBarItem.entries.forEach { navBarItem ->
                val selected = selectedNavBarItem == navBarItem
                val imageVector = if (selected) navBarItem.selectedImageVector else navBarItem.unselectedImageVector

                item(
                    selected = selected,
                    icon = { Icon(imageVector = imageVector, contentDescription = null) },
                    label = { navBarItem.text?.let { Text(text = it, maxLines = 1) } },
                    onClick = { viewModel?.onNavBarItemSelected(navBarItem) }
                )
            }
        },
    ) {
        val windowSize = if (isPreview) WindowSize.COMPACT else LocalContext.current.requireActivity().rememberWindowSize()
        val isLandscape = if (isPreview) false else LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        val appScaffoldModifier = if (isLandscape && windowSize == WindowSize.COMPACT) {
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .navigationBarsPadding() // prevent FAB and top app bar from being covered by OS nav bar in landscape
                .imePadding()
        } else {
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .imePadding()
        }

        Scaffold(
            topBar = topAppBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            contentWindowInsets = contentWindowInsets,
            modifier = appScaffoldModifier,
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}

/**
 * Per <a href="https://m3.material.io/components/navigation-drawer/guidelines">Material Design 3 guidelines</a>,
 * the selection of the appropriate navigation component should be contingent on the available
 * window size:
 * - Bottom Bar for compact window sizes (below 600dp)
 * - Navigation Rail for medium and expanded window sizes up to 1240dp (between 600dp and 1240dp)
 * - Navigation Drawer to window size above 1240dp
 */
fun getNavigationSuiteType(windowSize: DpSize): NavigationSuiteType {
    return if (windowSize.width > 1240.dp) {
        NavigationSuiteType.NavigationDrawer
    } else if (windowSize.width >= 600.dp) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteType.NavigationBar
    }
}

@Composable
private fun IntSize.toDpSize(): DpSize = with(LocalDensity.current) {
    DpSize(width.toDp(), height.toDp())
}
