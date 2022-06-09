package com.example.hm6_mvvm_koin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hm6_mvvm_koin.model.Person


@Database(entities = [Person::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao
}