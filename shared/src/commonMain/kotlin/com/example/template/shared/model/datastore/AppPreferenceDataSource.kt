package com.example.template.shared.model.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.template.shared.model.settings.InAppUpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.lds.mobile.ext.mapDistinct

class AppPreferenceDataSource(
    appDataStore: AppDataStore,
    private val appScope: CoroutineScope,
) {
    private val dataStore = appDataStore.datastore

    val previousQueriesFlow: Flow<List<String>> = dataStore.data.mapDistinct { preferences -> preferences[PREVIOUS_QUERIES]?.split(",").orEmpty() }
    suspend fun setPreviousQueries(value: List<String>) = dataStore.edit { it[PREVIOUS_QUERIES] = value.joinToString(",") }

    val regexTextFlow: Flow<String> = dataStore.data.mapDistinct { it[REGEX_TEXT] ?: "" }
    suspend fun setRegexText(value: String) = dataStore.edit { it[REGEX_TEXT] = value }

    val regexExpressionFlow: Flow<String> = dataStore.data.mapDistinct { it[REGEX_EXPRESSION] ?: "" }
    suspend fun setRegexExpression(value: String) = dataStore.edit { it[REGEX_EXPRESSION] = value }

    val multilineModeEnabledFlow: Flow<Boolean> = dataStore.data.mapDistinct { it[REGEX_MULTILINE_ENABLED] == true }
    fun setMultilineModeEnabledAsync(value: Boolean) = appScope.launch { dataStore.edit { it[REGEX_MULTILINE_ENABLED] = value } }

    val enforceNavigationBarContrastFlow: Flow<Boolean> = dataStore.data.mapDistinct { it[ENFORCE_NAVIGATION_BAR_CONTRAST] != false }
    fun setEnforceNavigationBarContrastAsync(value: Boolean) = appScope.launch { dataStore.edit { it[ENFORCE_NAVIGATION_BAR_CONTRAST] = value } }

    // In-app update type
    val inAppUpdateTypeFlow: Flow<InAppUpdateType> = dataStore.data.mapDistinct {
        val typeName = it[IN_APP_UPDATE_TYPE] ?: InAppUpdateType.NONE.name
        runCatching { InAppUpdateType.valueOf(typeName) }.getOrDefault(InAppUpdateType.NONE)
    }

    fun setInAppUpdateTypeAsync(value: InAppUpdateType) = appScope.launch { dataStore.edit { it[IN_APP_UPDATE_TYPE] = value.name } }

    companion object {
        private val ENFORCE_NAVIGATION_BAR_CONTRAST = booleanPreferencesKey("ENFORCE_NAVIGATION_BAR_CONTRAST")
        private val IN_APP_UPDATE_TYPE = stringPreferencesKey("IN_APP_UPDATE_TYPE")
        private val PREVIOUS_QUERIES = stringPreferencesKey("PREVIOUS_SEARCHES")
        private val REGEX_EXPRESSION = stringPreferencesKey("REGEX_EXPRESSION")
        private val REGEX_MULTILINE_ENABLED = booleanPreferencesKey("REGEX_MULTILINE_ENABLED")
        private val REGEX_TEXT = stringPreferencesKey("REGEX_TEXT")
    }
}
