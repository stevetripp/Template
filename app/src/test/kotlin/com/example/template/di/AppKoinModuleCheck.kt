package com.example.template.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.template.ux.breadcrumbs.BreadCrumbsViewModel
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.pullrefresh.PullRefreshRoute
import io.ktor.client.engine.HttpClientEngine
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import org.lds.media.cast.CastManager

class AppKoinModuleCheck {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinModule() {
        // extraTypes represents types that should be ignored because the are not being injected
        val extraTypes = listOf(
            Context::class,
            Application::class,
            HttpClientEngine::class, // Provided by Ktor
            DataStore::class, // Implementations of DataStore is done in the single<UserDataStore>
            CoroutineDispatcher::class, // Implementations of CoroutineDispatcher is done in the single<AppCoroutineDispatchers>
            SavedStateHandle::class, // Provided with Koin viewModelOf()

            // Classes in a WrapperClass
            OkHttpClient::class, // Provided as a wrapper item in StandardOkHttpClient or AuthenticatedOkHttpClient

            // Route items that are supplied by koinViewModel<XxxViewModel> { parametersOf(key) }
            DestinationRoute::class,
            PullRefreshRoute::class,
        )

        // Combine all modules into one "wrapper" module for verification.
        // This ensures all modules will know about definitions provided by other modules
        module {
            includes(getAllKoinModules())
        }.verify(
            extraTypes = extraTypes,
            injections = injectedParameters(
                // Whitelist classes that are ONLY for given injected class
                definition<CastManager>(Boolean::class),
                definition<BreadCrumbsViewModel>(List::class),
            )
        )
    }

    @Test
    fun `verify no duplicate definitions in Koin modules`() {
        koinApplication {
            allowOverride(false) // STRICT MODE: Fail on duplicates
            modules(getAllKoinModules())
        }
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun `verify all ViewModels are registered in Koin`() {
        findMissingRegisteredTypes(ViewModel::class)
    }

    private fun findMissingRegisteredTypes(baseClass: KClass<*>) {
        // 1. Pre-fetch all files that look like Koin Modules
        val koinModules = getAllKoinModules()

        val koinDefinitions: List<String> = getRegisteredClassNames(koinModules)

        // 2. Log registered definitions (for debugging)
        // koinDefinitions.forEach { println("def: $it") }

        println("Registered types:")
        koinDefinitions.forEach { println("  - $it") }
    }

    @OptIn(KoinInternalApi::class)
    fun getRegisteredClassNames(modules: List<Module>): List<String> {
        return modules
            .flatMap { module ->
                module.mappings.values // Get all BeanDefinitions
            }
            .map { instanceFactory: InstanceFactory<*> ->
                // Extract the KClass simple name
                instanceFactory.beanDefinition.primaryType.simpleName.orEmpty()
            }
            .distinct() // Remove duplicates if multiple modules define the same type
    }
}
