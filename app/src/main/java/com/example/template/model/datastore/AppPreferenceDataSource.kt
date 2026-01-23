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
import com.example.template.ux.settings.InAppUpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.lds.mobile.ext.mapDistinct

class AppPreferenceDataSource(
    private val application: Application,
    private val appScope: CoroutineScope,
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

    val previousQueriesFlow: Flow<List<String>> = application.dataStore.data.mapDistinct { preferences -> preferences[PREVIOUS_QUERIES]?.split(",").orEmpty() }
    suspend fun setPreviousQueries(value: List<String>) = application.dataStore.edit { it[PREVIOUS_QUERIES] = value.joinToString(",") }

    val regexTextFlow: Flow<String> = application.dataStore.data.mapDistinct { it[REGEX_TEXT] ?: "" }
    suspend fun setRegexText(value: String) = application.dataStore.edit { it[REGEX_TEXT] = value }

    val regexExpressionFlow: Flow<String> = application.dataStore.data.mapDistinct { it[REGEX_EXPRESSION] ?: "" }
    suspend fun setRegexExpression(value: String) = application.dataStore.edit { it[REGEX_EXPRESSION] = value }

    val multilineModeEnabledFlow: Flow<Boolean> = application.dataStore.data.mapDistinct { it[REGEX_MULTILINE_ENABLED] == true }
    fun setMultilineModeEnabledAsync(value: Boolean) = appScope.launch { application.dataStore.edit { it[REGEX_MULTILINE_ENABLED] = value } }

    val enforceNavigationBarContrastFlow: Flow<Boolean> = application.dataStore.data.mapDistinct { it[ENFORCE_NAVIGATION_BAR_CONTRAST] != false }
    fun setEnforceNavigationBarContrastAsync(value: Boolean) = appScope.launch { application.dataStore.edit { it[ENFORCE_NAVIGATION_BAR_CONTRAST] = value } }

    // In-app update type
    val inAppUpdateTypeFlow: Flow<InAppUpdateType> = application.dataStore.data.mapDistinct {
        val typeName = it[IN_APP_UPDATE_TYPE] ?: InAppUpdateType.NONE.name
        runCatching { InAppUpdateType.valueOf(typeName) }.getOrDefault(InAppUpdateType.NONE)
    }

    fun setInAppUpdateTypeAsync(value: InAppUpdateType) = appScope.launch { application.dataStore.edit { it[IN_APP_UPDATE_TYPE] = value.name } }

    companion object {
        private val ENFORCE_NAVIGATION_BAR_CONTRAST = booleanPreferencesKey("ENFORCE_NAVIGATION_BAR_CONTRAST")
        private val IN_APP_UPDATE_TYPE = stringPreferencesKey("IN_APP_UPDATE_TYPE")
        private val PREVIOUS_QUERIES = stringPreferencesKey("PREVIOUS_SEARCHES")
        private val REGEX_EXPRESSION = stringPreferencesKey("REGEX_EXPRESSION")
        private val REGEX_MULTILINE_ENABLED = booleanPreferencesKey("REGEX_MULTILINE_ENABLED")
        private val REGEX_TEXT = stringPreferencesKey("REGEX_TEXT")
    }
}