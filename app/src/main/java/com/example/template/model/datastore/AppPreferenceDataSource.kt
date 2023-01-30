package com.example.template.model.datastore

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.template.ext.ProcessScope
import com.example.template.ui.widget.PermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
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


    val locationPermissionStateFlow: Flow<PermissionState> = application.dataStore.data.mapLatest { preferences ->
        preferences[Key.LOCATION_PERMISSION_STATE]?.let { PermissionState.valueOf(it) } ?: PermissionState.NotPermitted
    }

    suspend fun setLocationPermissionState(value: PermissionState) = application.dataStore.edit { it[Key.LOCATION_PERMISSION_STATE] = value.name }
    fun setLocationPermissionStateAsync(value: PermissionState) = ProcessScope.launch { application.dataStore.edit { it[Key.LOCATION_PERMISSION_STATE] = value.name } }

    object Key {
        val IGNORE_LOCATION_PERMISSION_REQUEST = booleanPreferencesKey("IGNORE_LOCATION_PERMISSION_REQUEST")
        val LOCATION_PERMISSION_STATE = stringPreferencesKey("LOCATION_PERMISSION_STATE")
    }
}