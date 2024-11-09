package com.example.template.ux.breadcrumbs

import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute

@Serializable
data class BreadCrumb(
    val route: NavigationRoute,
    val title: String
)
