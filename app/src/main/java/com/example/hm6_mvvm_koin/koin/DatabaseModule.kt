package com.example.hm6_mvvm_koin.koin

import androidx.room.Room
import com.example.hm6_mvvm_koin.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(), //дает нам уже готовый из койна
            AppDatabase::class.java,
            "app_database"
        )
            .build()
    }

    single {
        get<AppDatabase>().personDao()
    }
}