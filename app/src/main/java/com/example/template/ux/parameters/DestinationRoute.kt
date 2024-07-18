package com.example.template.ux.parameters

import com.example.template.ux.NavTypeMaps
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute
import kotlin.reflect.typeOf

@Serializable
data class DestinationRoute(
    val param1: Parameter1,
    val enumParam: EnumParameter,
    val param2: Parameter2? = null
) : NavigationRoute {
    companion object {
        const val REQUIRED_PARAMETER = "param1"
        const val OPTIONAL_PARAMETER = "param2"
    }
}

fun DestinationRoute.Companion.typeMap() = mapOf(
    typeOf<Parameter1>() to NavTypeMaps.Parameter1NavType,
    typeOf<Parameter2?>() to NavTypeMaps.Parameter2NullableNavType,
    typeOf<EnumParameter>() to NavTypeMaps.EnumParameterNavType,
)


@JvmInline
@Serializable
value class Parameter1(val value: String)

@JvmInline
@Serializable
value class Parameter2(val value: String)

enum class EnumParameter(val value: String) {
    ONE("One"),
    TWO("Two"),
    THREE("Three");

    override fun toString(): String = "$name, $value"
}
