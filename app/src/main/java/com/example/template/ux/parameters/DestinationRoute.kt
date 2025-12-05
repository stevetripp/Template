package com.example.template.ux.parameters

import androidx.navigation3.runtime.NavKey
import com.example.template.util.AppDeepLinks
import io.ktor.http.Url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation.RouteUtil
import org.lds.mobile.navigation3.DeepLinkPattern


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
    @SerialName(DeepLinkArgs.QUERY_PARAM3)
    val optParam3: Int? = null,
    val closeOnBack: Boolean = false
) : NavKey

// https://trippntechnology.com/template/DESTINATION/{pathParam1}/{pathParam2}?queryParam1=value&queryParam2=value&queryParam3=value
val DestinationRoute.Companion.deepLinkPattern
    get() = DeepLinkPattern(
        Url(
            "${AppDeepLinks.ROOT}/DESTINATION/${RouteUtil.defineArg(DeepLinkArgs.PATH_PARAM1)}/${RouteUtil.defineArg(DeepLinkArgs.PATH_PARAM2)}" +
                    "?${RouteUtil.defineOptionalArgs(DeepLinkArgs.QUERY_PARAM1, DeepLinkArgs.QUERY_PARAM2, DeepLinkArgs.QUERY_PARAM3)}"
        )
    )


object DeepLinkArgs {
    const val PATH_PARAM1 = "pathParam1"
    const val PATH_PARAM2 = "pathParam2"
    const val QUERY_PARAM1 = "queryParam1"
    const val QUERY_PARAM2 = "queryParam2"
    const val QUERY_PARAM3 = "queryParam3"
}

@JvmInline
@Serializable
value class Parameter1(val value: String)

enum class EnumParameter(val value: String) {
    ONE("One"),
    TWO("Two"),
    THREE("Three");
}
