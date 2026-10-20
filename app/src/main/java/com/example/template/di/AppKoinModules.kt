package com.example.template.di

import com.example.template.shared.di.getSharedKoinModules
import org.koin.core.module.Module

fun getAllKoinModules(): List<Module> = buildList {
    add(appModule)
    add(mediaModule)
    add(viewModelModule)
    addAll(getSharedKoinModules())
}
