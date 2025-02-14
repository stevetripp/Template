package com.example.template.ux.parameters

import androidx.navigation.navDeepLink
import com.example.template.ux.DeepLink
import com.example.template.ux.NavTypeMaps
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.NavigationRoute
import org.lds.mobile.navigation.RouteUtil
import kotlin.reflect.typeOf


@Serializable
data class DestinationRoute(
    @SerialName(DeepLinkArgs.PATH_PARAM1)
    val reqParam1: Parameter1,
    @SerialName(DeepLinkArgs.PATH_PARAM2)
    val reqParam2: EnumParameter,
    @SerialName(DeepLinkArgs.QUERY_PARAM1)
    val optParam1: Parameter1? = null,
    @SerialName(DeepLinkArgs.QUERY_PARAM2)
    val optParam2: EnumParameter? = null,
) : NavigationRoute

fun DestinationRoute.Companion.typeMap() = mapOf(
    typeOf<Parameter1>() to NavTypeMaps.Parameter1NavType,
    typeOf<Parameter1?>() to NavTypeMaps.Parameter1NullableNavType,
    typeOf<EnumParameter>() to NavTypeMaps.EnumParameterNavType,
    typeOf<EnumParameter?>() to NavTypeMaps.EnumParameterNullableNavType,
)

private object DeepLinkArgs {
    const val PATH_PARAM1 = "pathParam1"
    const val PATH_PARAM2 = "pathParam2"
    const val QUERY_PARAM1 = "queryParam1"
    const val QUERY_PARAM2 = "queryParam2"
}

fun DestinationRoute.Companion.deepLinks() = listOf(
    navDeepLink {
        uriPattern = "${DeepLink.ROOT}/PARAMETERS/${RouteUtil.defineArg(DeepLinkArgs.PATH_PARAM1)}/${RouteUtil.defineArg(DeepLinkArgs.PATH_PARAM2)}" +
                "?${RouteUtil.defineOptionalArgs(DeepLinkArgs.QUERY_PARAM1, DeepLinkArgs.QUERY_PARAM2)}"
    }
)

@JvmInline
@Serializable
value class Parameter1(val value: String)

enum class EnumParameter(val value: String) {
    ONE("One"),
    TWO("Two"),
    THREE("Three");

    override fun toString(): String = "$name, $value"
}