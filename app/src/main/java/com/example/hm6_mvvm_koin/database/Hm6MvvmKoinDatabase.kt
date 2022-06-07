package com.example.hm6_mvvm_koin.database

import android.app.Application
import android.content.Context
import androidx.room.Room

class Hm6MvvmKoinDatabase : Application() {

    private var _dataBase: AppDatabase? = null
    val dataBase
        get() = requireNotNull(_dataBase) {
            "Some troubles with Database"
        }

    override fun onCreate() {
        super.onCreate()

        //db init
        _dataBase = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "app_database"
        )
            .allowMainThreadQueries()
            .build()
    }
}

val Context.appDataBase: AppDatabase
    get() = when {
        this is Hm6MvvmKoinDatabase -> dataBase
        else -> applicationContext.appDataBase
    }