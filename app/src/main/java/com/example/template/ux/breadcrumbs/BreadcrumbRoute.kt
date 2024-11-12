package com.example.template.ux.breadcrumbs

import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
abstract class BreadcrumbRoute(
    open val breadcrumbTitle: String
) : NavigationRoute