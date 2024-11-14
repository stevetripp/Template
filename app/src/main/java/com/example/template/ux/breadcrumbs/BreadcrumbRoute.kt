package com.example.template.ux.breadcrumbs

import org.lds.mobile.navigation.NavigationRoute

interface BreadcrumbRoute : NavigationRoute {
    val title: String
}