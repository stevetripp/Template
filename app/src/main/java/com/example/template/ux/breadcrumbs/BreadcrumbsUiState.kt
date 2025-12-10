package com.example.template.ux.breadcrumbs

data class BreadcrumbsUiState(
    val breadcrumbRoutes: List<BreadcrumbRoute> = emptyList(),
    val onBreadCrumbClicked: (BreadcrumbRoute) -> Unit = {},
    val onNavigate: () -> Unit = {},
)