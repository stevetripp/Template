package org.ics.mobile.commons

import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.encodedPath
import io.ktor.http.formUrlEncode

/**
 * A KMP-friendly URI wrapper backed by Ktor's URLBuilder.
 * Supports both absolute URLs (https://example.com) and relative URIs (/path?query).
 */
class Uri private constructor(
    private val delegate: Url,
    private val isRelative: Boolean
) {
    val path: String get() = delegate.encodedPath
    val scheme: String? get() = if (isRelative) null else delegate.protocol.name
    val host: String? get() = if (isRelative) null else delegate.host
    val port: Int get() = delegate.port
    val fragment: String get() = delegate.fragment

    // Mimic Android's Uri.pathSegments behavior
    val pathSegments: List<String>get() = path.trimStart('/').split('/').filter { it.isNotEmpty() }

    // Easy parameter access
    val parameters: Parameters get() = delegate.parameters
    fun getQueryParameter(key: String): String? = parameters[key]
    fun getQueryParameters(key: String): List<String>? = parameters.getAll(key)

    /**
     * Creates a mutable Builder initialized with this Uri's state.
     */
    fun newBuilder(): Builder {
        return Builder(URLBuilder(delegate), isRelative)
    }

    override fun toString(): String {
        return if (isRelative) {
            buildString {
                append(delegate.encodedPath)
                if (!delegate.parameters.isEmpty()) {
                    append("?")
                    append(delegate.parameters.formUrlEncode())
                }
                if (delegate.fragment.isNotEmpty()) {
                    append("#")
                    append(delegate.fragment)
                }
            }
        } else {
            delegate.toString()
        }
    }

    companion object {
        private const val DUMMY_BASE = "dummy://dummy"

        fun parse(uriString: String): Uri {
            val isRelative = !uriString.contains("://")
            val parseTarget = if (isRelative) "$DUMMY_BASE$uriString" else uriString

            return try {
                Uri(Url(parseTarget), isRelative)
            } catch (ignore: Exception) {
                // Fallback for empty or extremely malformed strings
                Uri(Url(DUMMY_BASE), true)
            }
        }
    }

    /**
     * A Mutable Builder for KmpUri.
     */
    class Builder internal constructor(
        private val urlBuilder: URLBuilder,
        private var isRelative: Boolean
    ) {
        constructor() : this(URLBuilder(DUMMY_BASE), true)

        fun scheme(scheme: String) = apply {
            urlBuilder.protocol = URLProtocol.createOrDefault(scheme)
            isRelative = false // If you set a scheme, it's no longer relative
        }

        fun host(host: String) = apply {
            urlBuilder.host = host
            isRelative = false // If you set a host, it implies absolute
        }

        fun path(path: String) = apply {
            urlBuilder.encodedPath = path
        }

        fun appendPath(segment: String) = apply {
            urlBuilder.appendPathSegments(segment)
        }

        fun clearQuery() = apply {
            urlBuilder.parameters.clear()
        }

        fun appendQueryParameter(key: String, value: String?) = apply {
            if (value != null) {
                urlBuilder.parameters.append(key, value)
            }
        }

        fun setQueryParameter(key: String, value: String?) = apply {
            if (value != null) {
                urlBuilder.parameters[key] = value // Replaces existing
            } else {
                urlBuilder.parameters.remove(key)
            }
        }

        fun fragment(fragment: String) = apply {
            urlBuilder.fragment = fragment
        }

        fun build(): Uri {
            return Uri(urlBuilder.build(), isRelative)
        }
    }
}

fun String.toUri(): Uri = Uri.parse(this)
