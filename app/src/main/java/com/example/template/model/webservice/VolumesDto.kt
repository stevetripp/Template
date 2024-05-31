package com.example.template.model.webservice

import kotlinx.serialization.Serializable

@Serializable
data class VolumesDto(
    val totalItems: Int,
    val kind: String,
    val items: List<ItemsItem>
)

@Serializable
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
)

@Serializable
data class ItemsItem(
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val kind: String,
    val volumeInfo: VolumeInfo,
    val etag: String,
    val id: String,
    val accessInfo: AccessInfo,
    val selfLink: String
)

@Serializable
data class IndustryIdentifiersItem(
    val identifier: String,
    val type: String
)

@Serializable
data class ImageLinks(
    val thumbnail: String,
    val smallThumbnail: String
)

@Serializable
data class SearchInfo(
    val textSnippet: String
)

@Serializable
data class Pdf(
    val isAvailable: Boolean,
//    val downloadLink: String
)

@Serializable
data class SaleInfo(
    val country: String,
    val isEbook: Boolean,
    val saleability: String,
//    val buyLink: String
)

@Serializable
data class AccessInfo(
    val accessViewStatus: String,
    val country: String,
    val viewability: String,
    val pdf: Pdf,
    val webReaderLink: String,
    val epub: Epub,
    val publicDomain: Boolean,
    val quoteSharingAllowed: Boolean,
    val embeddable: Boolean,
    val textToSpeechPermission: String
)

@Serializable
data class VolumeInfo(
//    val industryIdentifiers: List<IndustryIdentifiersItem>,
    val pageCount: Int,
    val printType: String,
    val readingModes: ReadingModes,
    val previewLink: String,
    val canonicalVolumeLink: String,
    val language: String,
    val title: String,
    val imageLinks: ImageLinks,
    val panelizationSummary: PanelizationSummary,
    val publishedDate: String,
//    val categories: List<String>,
    val maturityRating: String,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val infoLink: String,
//    val authors: List<String>
)

@Serializable
data class PanelizationSummary(
    val containsImageBubbles: Boolean,
    val containsEpubBubbles: Boolean
)

@Serializable
data class Epub(
    val isAvailable: Boolean,
//    val downloadLink: String
)

