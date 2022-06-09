package com.example.hm6_mvvm_koin.database

import android.app.Application
import com.example.hm6_mvvm_koin.koin.databaseModule
import com.example.hm6_mvvm_koin.koin.networkModule
import com.example.hm6_mvvm_koin.koin.repositoryModule
import com.example.hm6_mvvm_koin.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Hm6MvvmKoin : Application() {

    override fun onCreate() {
        super.onCreate()

        //начало работы с koin.
        startKoin {
            androidContext(this@Hm6MvvmKoin)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}
