package com.example.template.di

import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.example.template.model.datastore.AppPreferenceDataSource
import com.example.template.model.webservice.KtorClientDefaults.defaultSetup
import com.example.template.model.webservice.KtorClientDefaults.setupStandardHeaders
import com.example.template.ux.chipsheet.ChipSheetViewModel
import com.example.template.ux.dialog.DialogViewModel
import com.example.template.ux.edgetoedge.EdgeToEdgeViewModel
import com.example.template.ux.fab.FabViewModel
import com.example.template.ux.gmailaddressfield.GmailAddressFieldViewModel
import com.example.template.ux.home.HomeViewModel
import com.example.template.ux.ktor.KtorViewModel
import com.example.template.ux.main.MainViewModel
import com.example.template.ux.memorize.MemorizeViewModel
import com.example.template.ux.modalsidesheet.ModalSideSheetViewModel
import com.example.template.ux.notification.NotificationUtil
import com.example.template.ux.notification.NotificationViewModel
import com.example.template.ux.notificationpermissions.NotificationPermissionsViewModel
import com.example.template.ux.parameters.DestinationViewModel
import com.example.template.ux.parameters.ParametersViewModel
import com.example.template.ux.permissions.PermissionsViewModel
import com.example.template.ux.popwithresult.PopWithResultChildViewModel
import com.example.template.ux.popwithresult.PopWithResultParentViewModel
import com.example.template.ux.regex.RegexViewModel
import com.example.template.ux.reorderablelist.ReorderableListViewModel
import com.example.template.ux.search.SearchViewModel
import com.example.template.ux.servicesexamples.ServicesExamplesViewModel
import com.example.template.ux.settings.SettingsViewModel
import com.example.template.ux.snackbar.SnackbarViewModel
import com.example.template.ux.startup.StartupViewModel
import com.example.template.ux.stickyheaders.StickyHeaderViewModel
import com.example.template.ux.synchronizescrolling.SynchronizeScrollingViewModel
import com.example.template.ux.video.player.PlayerViewModel
import com.example.template.ux.video.screen.VideoScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import java.io.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.lds.media.cast.CastManager
import org.lds.mobile.util.LdsDeviceUtil

fun getAllKoinModules(): List<Module> = buildList {
    add(coroutineModule)
    add(appModule)
    add(datastoreModule)
    add(mediaModule)
    add(serviceModule)
    add(viewModelModule)
}

// ===== Coroutine Module =====
interface CoroutineDispatchers {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

val coroutineModule = module {
    // ===== Scopes =====
    // ApplicationScope (No name on this one so that it IS the default one that is used if there is no name is provided)
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // ===== Dispatchers =====
    // IoDispatcher (No name on this one so that it IS the default one that is used if there is no name is provided)
    single<CoroutineDispatchers> {
        object : CoroutineDispatchers {
            override val default = Dispatchers.Default
            override val io = Dispatchers.IO
            override val main = Dispatchers.Main
        }
    }
}

// ===== App Module =====
val appModule = module {
    singleOf(::NotificationUtil)
    singleOf(::LdsDeviceUtil)

    // CastManager
    single<CastManager> { CastManager(get(), initCast = true) }

    // AppPreferenceDataSource
    single<AppPreferenceDataSource> { AppPreferenceDataSource(application = androidApplication(), appScope = get<CoroutineScope>()) }
}

// ===== DataStore Module =====
val datastoreModule = module {
    // AppPreferenceDataSource is provided by appModule
}

// ===== Media Module =====
val mediaModule = module {
    // Media3 DatabaseProvider
    single<DatabaseProvider> { StandaloneDatabaseProvider(androidApplication()) }

    // Media3 Download Cache
    single<Cache> {
        val downloadDirectory = androidApplication().getExternalFilesDir(null) ?: androidApplication().filesDir
        downloadDirectory.mkdirs()
        SimpleCache(File(downloadDirectory, "downloads"), NoOpCacheEvictor(), get<DatabaseProvider>())
    }
}

// ===== Service Module =====
val serviceModule = module {
    // Google Books API HttpClient
    single(/*qualifier = org.koin.core.qualifier.named("GOOGLE_API_STANDARD_CLIENT")*/) {
        HttpClient(OkHttp.create()) {
            install(Logging) { defaultSetup() }
            install(Resources)
            install(ContentNegotiation) { defaultSetup(allowAnyContentType = true) }
            defaultRequest {
                url("https://www.googleapis.com/books/")
                setupStandardHeaders()
            }
        }
    }
}

// ===== ViewModel Module =====
val viewModelModule = module {
    viewModelOf(::ChipSheetViewModel)
    viewModelOf(::DestinationViewModel)
    viewModelOf(::DialogViewModel)
    viewModelOf(::EdgeToEdgeViewModel)
    viewModelOf(::FabViewModel)
    viewModelOf(::GmailAddressFieldViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::KtorViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MemorizeViewModel)
    viewModelOf(::ModalSideSheetViewModel)
    viewModelOf(::NotificationPermissionsViewModel)
    viewModelOf(::NotificationViewModel)
    viewModelOf(::ParametersViewModel)
    viewModelOf(::PermissionsViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::PopWithResultChildViewModel)
    viewModelOf(::PopWithResultParentViewModel)
//    viewModelOf { ::PullRefreshViewModel }
    viewModelOf(::RegexViewModel)
    viewModelOf(::ReorderableListViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ServicesExamplesViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SnackbarViewModel)
    viewModelOf(::StartupViewModel)
    viewModelOf(::StickyHeaderViewModel)
    viewModelOf(::SynchronizeScrollingViewModel)
    viewModelOf(::VideoScreenViewModel)
}
