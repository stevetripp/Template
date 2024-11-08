package com.example.template.ux.breadcrumbs

import com.example.template.ux.NavTypeMaps
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute
import kotlin.reflect.typeOf

@Serializable
data class BreadCrumbsRoute(
    val level: Int = 0,
    val breadCrumbs: List<BreadCrumb>? = null,
) : BreadCrumbNavigationRoute()

fun BreadCrumbsRoute.Companion.typeMap() = mapOf(
    typeOf<List<BreadCrumb>?>() to NavTypeMaps.BreadCrumbListNullableNavType,
)
