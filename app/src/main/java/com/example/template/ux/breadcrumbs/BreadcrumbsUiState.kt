package com.example.template.ux.breadcrumbs

data class BreadcrumbsUiState(
    val onBreadCrumbClicked: (BreadcrumbRoute) -> Unit = {},
    val onNavigate: (List<BreadcrumbRoute>) -> Unit = {},
)