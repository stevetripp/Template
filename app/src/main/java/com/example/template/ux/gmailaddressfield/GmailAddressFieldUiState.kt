package com.example.template.ux.gmailaddressfield

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class GmailAddressFieldUiState(
    val contactsFlow: StateFlow<List<Contact>> = MutableStateFlow(emptyList()),
    val chipsFlow: StateFlow<List<Contact>> = MutableStateFlow(emptyList()),
    val setContactsReadPermission: (Boolean) -> Unit = {},
    val onContactClicked: (Contact) -> Unit = {},
)