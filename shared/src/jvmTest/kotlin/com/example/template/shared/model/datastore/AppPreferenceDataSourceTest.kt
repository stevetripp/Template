package com.example.template.shared.model.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.template.shared.model.settings.InAppUpdateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Runs against a real preferences DataStore on a temp file.
 */
@OptIn(ExperimentalUuidApi::class)
class AppPreferenceDataSourceTest {

    private lateinit var dataStoreDir: Path
    private lateinit var scope: CoroutineScope
    private lateinit var dataSource: AppPreferenceDataSource

    @BeforeTest
    fun setUp() {
        dataStoreDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "app-prefs-test-${Uuid.random()}"
        FileSystem.SYSTEM.createDirectories(dataStoreDir)
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore = PreferenceDataStoreFactory.createWithPath(scope = scope) { dataStoreDir / "app.preferences_pb" }
        dataSource = AppPreferenceDataSource(AppDataStore(dataStore), scope)
    }

    @AfterTest
    fun tearDown() {
        scope.cancel()
        FileSystem.SYSTEM.deleteRecursively(dataStoreDir, mustExist = false)
    }

    @Test
    fun `defaults are used when nothing is saved`() = runBlocking {
        assertEquals(emptyList(), dataSource.previousQueriesFlow.first())
        assertEquals("", dataSource.regexTextFlow.first())
        assertFalse(dataSource.multilineModeEnabledFlow.first())
        assertTrue(dataSource.enforceNavigationBarContrastFlow.first())
        assertEquals(InAppUpdateType.NONE, dataSource.inAppUpdateTypeFlow.first())
    }

    @Test
    fun `previous queries round trip`() = runBlocking {
        dataSource.setPreviousQueries(listOf("kotlin", "android"))

        assertEquals(listOf("kotlin", "android"), dataSource.previousQueriesFlow.first())
    }

    @Test
    fun `async setters persist`() = runBlocking {
        dataSource.setMultilineModeEnabledAsync(true).join()
        dataSource.setEnforceNavigationBarContrastAsync(false).join()
        dataSource.setInAppUpdateTypeAsync(InAppUpdateType.FLEXIBLE).join()

        assertTrue(dataSource.multilineModeEnabledFlow.first())
        assertFalse(dataSource.enforceNavigationBarContrastFlow.first())
        assertEquals(InAppUpdateType.FLEXIBLE, dataSource.inAppUpdateTypeFlow.first())
    }
}
