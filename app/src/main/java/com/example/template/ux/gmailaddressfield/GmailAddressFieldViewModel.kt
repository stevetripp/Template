package com.example.template.ux.gmailaddressfield

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GmailAddressFieldViewModel
@Inject constructor(
    private val application: Application,
) : ViewModel() {

    private val hasContactsReadPermissionsFlow = MutableStateFlow(false)
    private val chipsFlow = MutableStateFlow<List<Contact>>(emptyList())

    private val contactsFlow = hasContactsReadPermissionsFlow.mapLatest { hasPermission ->
        if (hasPermission) getContacts() else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    val uiState = GmailAddressFieldUiState(
        contactsFlow = contactsFlow,
        chipsFlow = chipsFlow,
        setContactsReadPermission = {
            Log.i("SMT", """setContactsReadPermission: $it""")
            hasContactsReadPermissionsFlow.value = it
        }
    ) { chipsFlow.value = chipsFlow.value.toMutableList().apply { add(it) } }

    private fun getContacts(): List<Contact> {
        Log.i("SMT", """getContacts()""")
        val contacts = mutableListOf<Contact>()
        val cr = application.contentResolver
        cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            SELECTION,
            null,
            SORT_ORDER
        ).use { cursor ->
            if (cursor == null) return@use
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val thumbnailUri = cursor.asString(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI).orEmpty()
                    val email = cursor.asString(ContactsContract.CommonDataKinds.Email.DATA).orEmpty()
                    val name = cursor.asString(ContactsContract.Contacts.DISPLAY_NAME).orEmpty()
                    contacts.add(Contact(thumbnailUri, name, email))
                }
            }
        }
        return contacts
    }

    private fun Cursor.asString(column: String): String? {
        return try {
            val index = getColumnIndex(column)
            if (index > 0) getString(index) else null
        } catch (expected: Exception) {
            null
        }
    }

    companion object {
        private val PROJECTION = arrayOf(
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID
        )
        private const val SELECTION = "${ContactsContract.CommonDataKinds.Email.DATA} <> ''"
        private const val SORT_ORDER = "CASE WHEN ${ContactsContract.Contacts.DISPLAY_NAME} NOT LIKE '%@%' THEN 1 ELSE 2 END, " +
                "${ContactsContract.Contacts.DISPLAY_NAME}, ${ContactsContract.CommonDataKinds.Email.DATA} COLLATE NOCASE"

    }
}