package com.example.template.ux.breadcrumbs

import androidx.navigation3.runtime.NavKey

interface BreadcrumbRoute : NavKey {
    val title: String
}