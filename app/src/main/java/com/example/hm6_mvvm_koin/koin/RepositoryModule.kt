package com.example.hm6_mvvm_koin.koin

import com.example.hm6_mvvm_koin.PersonRepository
import org.koin.dsl.module

val repositoryModule = module {

    //create PersonRepository
    single {
        PersonRepository(get(), get())
    }
}