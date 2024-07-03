package org.lds.mobile.navigation

@JvmInline
value class NavRoute(val value: String)

@JvmInline
value class NavRouteDefinition(val value: String) {
    fun asNavRoute(): NavRoute = NavRoute(value)
}

fun String.asNavRoute(): NavRoute = NavRoute(this)

fun String.asNavRouteDefinition(): NavRouteDefinition = NavRouteDefinition(this)
