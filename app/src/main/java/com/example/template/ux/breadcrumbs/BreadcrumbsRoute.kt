package com.example.template.ux.breadcrumbs

import kotlinx.serialization.Serializable

@Serializable
data class BreadcrumbsRoute(
    val title: String,
) : BreadcrumbRoute(title)
