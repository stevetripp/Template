package com.example.template.ux.video

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class VideoId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class VideoUrl(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class VideoRenditionType(val value: String) {
    init {
        require(value.isNotBlank())
    }
}




