package com.example.template.domain

import kotlinx.serialization.Serializable

@Serializable(with = VideoIdSerializer::class)
data class VideoId(override val value: String) : StringDataClass {
    init {
        require(value.isNotBlank()) { "VideoId must contain a value" }
    }
}

object VideoIdSerializer : StringDataClassSerializer<VideoId>("VideoIdSerializer", { VideoId(it) })

@Serializable(with = VideoUrlSerializer::class)
data class VideoUrl(override val value: String) : StringDataClass {
    init {
        require(value.isNotBlank()) { "VideoUrl must contain a value" }
    }
}

object VideoUrlSerializer : StringDataClassSerializer<VideoUrl>("VideoUrlSerializer", { VideoUrl(it) })

@Serializable(with = VideoRenditionTypeSerializer::class)
data class VideoRenditionType(override val value: String) : StringDataClass {
    init {
        require(value.isNotBlank()) { "VideoRenditionType must contain a value" }
    }
}

object VideoRenditionTypeSerializer : StringDataClassSerializer<VideoRenditionType>("VideoRenditionTypeSerializer", { VideoRenditionType(it) })

@Serializable(with = ParameterSerializer::class)
data class Parameter(override val value: String) : StringDataClass {
    init {
        require(value.isNotBlank()) { "Parameter must contain a value" }
    }
}

object ParameterSerializer : StringDataClassSerializer<Parameter>("ParameterSerializer", { Parameter(it) })


