package com.example.template.shared.di

import com.example.template.shared.model.repository.NoteRepository
import com.example.template.shared.model.repository.NoteRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}
