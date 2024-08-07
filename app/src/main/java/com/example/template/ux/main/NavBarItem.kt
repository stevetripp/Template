package com.example.template.ux.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.template.ux.about.AboutRoute
import com.example.template.ux.home.HomeRoute
import com.example.template.ux.servicesexamples.ServicesExamplesRoute
import org.lds.mobile.navigation.NavigationRoute

enum class NavBarItem(
    val unselectedImageVector: ImageVector,
    val selectedImageVector: ImageVector,
    val route: NavigationRoute,
    val text: String? = null,
) {
    UI_EXAMPLES(Icons.Outlined.Lightbulb, Icons.Filled.Lightbulb, HomeRoute, "UI Examples"),
    SERVICE_EXAMPLES(Icons.Outlined.MiscellaneousServices, Icons.Filled.MiscellaneousServices, ServicesExamplesRoute, "Services Examples"),
    ABOUT(Icons.Outlined.Info, Icons.Filled.Info, AboutRoute, "About");

    companion object {
        fun getNavBarItemRouteMap(): Map<NavBarItem, NavigationRoute> {
            return entries.associateWith { item -> item.route }
        }
    }
}