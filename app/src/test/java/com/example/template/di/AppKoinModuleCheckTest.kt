package com.example.template.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.template.ux.breadcrumbs.BreadCrumbsViewModel
import com.example.template.ux.parameters.DestinationRoute
import com.example.template.ux.pullrefresh.PullRefreshRoute
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutAbstractModifier
import com.lemonappdev.konsist.api.ext.list.withAllParentsOf
import com.lemonappdev.konsist.api.verify.assertTrue
import io.ktor.client.engine.HttpClientEngine
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import org.lds.media.cast.CastManager

class AppKoinModuleCheckTest {

    @Test
    fun checkKoinModule() {
        val extraTypes = listOf(
            // These types are ignored by the Koin module check and don't need to be verified
            Application::class,
            Context::class,
            CoroutineDispatcher::class, // Implementations of CoroutineDispatcher is done in the single<AppCoroutineDispatchers>
            DataStore::class, // Implementations of DataStore is done in the single<UserDataStore>
            DestinationRoute::class,
            HttpClientEngine::class, // Provided by Ktor
            OkHttpClient::class, // Provided as a wrapper item in StandardOkHttpClient or AuthenticatedOkHttpClient
            PullRefreshRoute::class,
            SavedStateHandle::class, // Provided with Koin viewModelOf()
        )

        module {
            includes(getAllKoinModules())
        }.verify(
            extraTypes = extraTypes,
            injections = injectedParameters(
                // These types are used by specific classes and are ignored from verification
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

//    @Test
//    fun `verify all CoroutineWorker are registered in Koin`() {
//        findMissingRegisteredTypes(CoroutineWorker::class)
//    }

    @Test
    fun `verify all ViewModels are registered in Koin`() {
        findMissingRegisteredTypes(ViewModel::class)
    }

    private fun findMissingRegisteredTypes(baseClass: KClass<*>) {
        val koinDefinitions: List<String> = getRegisteredClassNames(getAllKoinModules())

        Konsist.scopeFromProject()
            .classes()
            .withAllParentsOf(baseClass, indirectParents = true)
            .withoutAbstractModifier()
            .assertTrue { classDeclaration ->
                val isRegistered = koinDefinitions.any { it == classDeclaration.name }

                if (!isRegistered) {
                    println("FAILED: ${classDeclaration.name} is not registered in any Koin module.")
                }
                isRegistered
            }
    }

    @OptIn(KoinInternalApi::class)
    fun getRegisteredClassNames(modules: List<Module>): List<String> {
        return modules
            .flatMap { module -> module.mappings.values }
            .map { instanceFactory: InstanceFactory<*> -> instanceFactory.beanDefinition.primaryType.simpleName.orEmpty() }
            .distinct()
    }
}
