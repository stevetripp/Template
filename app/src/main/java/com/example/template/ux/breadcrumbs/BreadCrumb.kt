package com.example.template.ux.breadcrumbs

import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
data class BreadCrumb(val route: BreadCrumbNavigationRoute, val title: String)

/**
 * [BreadCrumbNavigationRoute] created so that Serializable could be added.
 */
@Serializable
open class BreadCrumbNavigationRoute : NavigationRoute