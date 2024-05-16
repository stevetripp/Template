package com.example.template.ux.video

@JvmInline
value class VideoId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class VideoUrl(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class VideoRenditionType(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class Iso3Locale(val value: String) {
    init {
        require(value.isNotBlank())
    }
}




