package com.example.template.ux.breadcrumbs

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BreadCrumbsUiState(
    val breadCrumbsFlow: StateFlow<List<BreadCrumb>> = MutableStateFlow(emptyList()),
    val titleFlow: StateFlow<String> = MutableStateFlow("Title"),

    val onBreadCrumbClicked: (BreadCrumb) -> Unit = {},
    val onNavigate: () -> Unit = {},
    val onNavController:(NavController)->Unit = {},
)