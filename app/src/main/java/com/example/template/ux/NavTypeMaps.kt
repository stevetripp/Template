package com.example.template.ux

import android.os.Bundle
import androidx.navigation.NavType
import com.example.template.ux.parameters.EnumParameter
import com.example.template.ux.parameters.Parameter1
import com.example.template.ux.parameters.Parameter2
import com.example.template.ux.video.VideoId
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Mappings for type safe navigation
 *
 * Notes:
 * 1. parseValue() should know how to parse what is returned from serializeAsValue() (format doesn't really matter... doesn't need to be json)
 * 2. If value for NavType<XXX> is complex (class with multiple variables) use Json.encodeToString<XXX>(value) and Json.decodeFromString<XXX>(value)
 */
object NavTypeMaps {
    val Parameter1NavType = object : NavType<Parameter1>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Parameter1? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): Parameter1 = Parameter1(value)
        override fun put(bundle: Bundle, key: String, value: Parameter1) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: Parameter1): String = value.value
    }

    val Parameter2NullableNavType = object : NavType<Parameter2?>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): Parameter2? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): Parameter2? = if (value.isBlank()) null else Parameter2(value)
        override fun put(bundle: Bundle, key: String, value: Parameter2?) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: Parameter2?): String = value?.value.orEmpty()
    }

    val VideoIdNavType = object : NavType<VideoId>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): VideoId? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): VideoId = VideoId(value)
        override fun put(bundle: Bundle, key: String, value: VideoId) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: VideoId): String = value.value
    }

    @Deprecated("Remove (shouldn't be needed for enums https://issuetracker.google.com/issues/346475493")
    val EnumParameterNavType = object : NavType<EnumParameter>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): EnumParameter? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): EnumParameter = Json.decodeFromString<EnumParameter>(value)
        override fun put(bundle: Bundle, key: String, value: EnumParameter) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: EnumParameter): String = Json.encodeToString(value)
    }

    @Deprecated("Remove (shouldn't be needed for enums https://issuetracker.google.com/issues/346475493")
    val EnumParameterNullableNavType = object : NavType<EnumParameter?>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): EnumParameter? = bundle.getString(key)?.let { parseValue(it) }
        override fun parseValue(value: String): EnumParameter = Json.decodeFromString<EnumParameter>(value)
        override fun put(bundle: Bundle, key: String, value: EnumParameter?) = bundle.putString(key, serializeAsValue(value))
        override fun serializeAsValue(value: EnumParameter?): String = Json.encodeToString(value)
    }
}
