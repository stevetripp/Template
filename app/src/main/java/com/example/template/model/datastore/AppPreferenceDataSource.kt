package com.example.template.model.datastore

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceDataSource
@Inject constructor(
    private val application: Application,
) {
    private val Context.dataStore by preferencesDataStore(
        name = "app",
        corruptionHandler = ReplaceFileCorruptionHandler {
            Log.e("Template", "Datastore Corrupted", it)
            emptyPreferences()
        },
//        produceMigrations = { applicationContext ->
//            listOf(
//                PreferenceMigrations.sharedPreferenceMigration(applicationContext, toVersion = 1, migrate = { sharedPrefs, currentData ->
//                    SharedPreferenceMigration.migrateSharedPreferences(sharedPrefs, currentData)
//                })
//            )
//        }
    )

    object Key {
    }
}