package com.example.template.ux.breadcrumbs

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BreadcrumbsUiState(
    val breadcrumbRoutesFlow: StateFlow<List<BreadcrumbRoute>> = MutableStateFlow(emptyList()),
    val titleFlow: StateFlow<String> = MutableStateFlow("Title"),

    val onBreadCrumbClicked: (BreadcrumbRoute) -> Unit = {},
    val onNavigate: () -> Unit = {},
)