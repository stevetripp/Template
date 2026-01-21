package com.example.template.ux.parameters

import androidx.navigation3.runtime.NavKey
import com.example.template.domain.Parameter
import com.example.template.util.DeepLinkConstants
import com.example.template.util.ext.addPathSegmentVariables
import com.example.template.util.ext.addPathSegments
import com.example.template.util.ext.addQueryParameterVariables
import io.ktor.http.Url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lds.mobile.navigation3.DeepLinkPattern


@Serializable
data class DestinationRoute(
    @SerialName(DeepLinkArgs.PATH_PARAM1)
    val reqParam1: Parameter,
    @SerialName(DeepLinkArgs.PATH_PARAM2)
    val reqParam2: EnumParameter,
    @SerialName(DeepLinkArgs.QUERY_PARAM1)
    val optParam1: Parameter? = null,
    @SerialName(DeepLinkArgs.QUERY_PARAM2)
    val optParam2: EnumParameter? = null,
    @SerialName(DeepLinkArgs.QUERY_PARAM3)
    val optParam3: Int? = null,
    val closeOnBack: Boolean = false
) : NavKey

// adb shell am start -a android.intent.action.VIEW -d 'https://trippntechnology.com/template/DESTINATION/value1/TWO?queryParam1=firstvalue&queryParam2=ONE'
// adb shell am start -a android.intent.action.VIEW -d 'myscheme://template/DESTINATION/value1/TWO?queryParam1=firstvalue&queryParam2=TWO'

val DestinationRoute.Companion.deepLinkPatterns
    get() = listOf(
        DeepLinkPattern(Url(DeepLinkConstants.HTTPS_ROOT))
            .addPathSegments(DeepLinkConstants.PATH_PREFIX, "DESTINATION")
            .addPathSegmentVariables(DeepLinkArgs.PATH_PARAM1, DeepLinkArgs.PATH_PARAM2)
            .addQueryParameterVariables(DeepLinkArgs.QUERY_PARAM1, DeepLinkArgs.QUERY_PARAM2, DeepLinkArgs.QUERY_PARAM3),
        DeepLinkPattern(Url(DeepLinkConstants.CUSTOM_ROOT))
            .addPathSegments("DESTINATION")
            .addPathSegmentVariables(DeepLinkArgs.PATH_PARAM1, DeepLinkArgs.PATH_PARAM2)
            .addQueryParameterVariables(DeepLinkArgs.QUERY_PARAM1, DeepLinkArgs.QUERY_PARAM2, DeepLinkArgs.QUERY_PARAM3)
    )

object DeepLinkArgs {
    const val PATH_PARAM1 = "pathParam1"
    const val PATH_PARAM2 = "pathParam2"
    const val QUERY_PARAM1 = "queryParam1"
    const val QUERY_PARAM2 = "queryParam2"
    const val QUERY_PARAM3 = "queryParam3"
}

enum class EnumParameter(val value: String) {
    ONE("One"),
    TWO("Two"),
    THREE("Three");
}
