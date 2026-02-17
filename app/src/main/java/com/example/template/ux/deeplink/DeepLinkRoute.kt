package com.example.template.ux.deeplink

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DeepLinkRoute(
    val requiredValue: String,
    val optionalValue: String? = null,
) : NavKey
