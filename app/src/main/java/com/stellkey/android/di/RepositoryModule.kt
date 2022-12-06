package com.stellkey.android.di

import com.stellkey.android.repository.UserRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single { UserRepository(get()) }
}