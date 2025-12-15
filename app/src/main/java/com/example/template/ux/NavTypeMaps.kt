package com.example.template.ux

import android.os.Bundle
import androidx.navigation.NavType
import com.example.template.ux.video.VideoId
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Mappings for type safe navigation
 *
 * Notes:
 * 1. parseValue() should know how to parse what is returned from serializeAsValue() (format doesn't really matter... doesn't need to be json)
 * 2. If value for NavType<XXX> is complex (class with multiple variables) use Json.encodeToString<XXX>(value) and Json.decodeFromString<XXX>(value)
 */
object NavTypeMaps {
    val VideoIdNavType = object : NavType<VideoId>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): VideoId? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): VideoId = VideoId(value)
        override fun put(bundle: Bundle, key: String, value: VideoId) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: VideoId): String = value.value
    }

    /**
     * Map of Kotlin types to their corresponding NavType implementations. Uses KType objects from typeOf<> as keys for direct type mapping.  Includes both non-nullable and nullable types.
     */
    @Suppress("UNCHECKED_CAST")
    val typeMap: Map<KType, NavType<*>> = mapOf(
        // Non-nullable types
        typeOf<VideoId>() to VideoIdNavType,

        // Nullable types
    )
}
