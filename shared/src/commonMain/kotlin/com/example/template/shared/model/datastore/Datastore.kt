package com.example.template.shared.model.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import co.touchlab.kermit.Logger
import com.example.template.shared.util.file.AppFileSystem

object DatastoreUtil {
    // Matches the name previously passed to Context.preferencesDataStore() so existing installs keep their preferences
    const val APP = "app"

    fun create(name: String): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = ReplaceFileCorruptionHandler {
            Logger.e(it) { "Datastore [$name] Corrupted" }
            emptyPreferences()
        },
        produceFile = { AppFileSystem.getDatastorePath(name) },
    )
}

class AppDataStore(val datastore: DataStore<Preferences>)
