package com.example.template.ux.breadcrumbs

import kotlinx.serialization.Serializable

@Serializable
data class BreadcrumbsRoute(
    override val title: String,
) : BreadcrumbRoute
