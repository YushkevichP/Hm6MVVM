package com.example.hm6_mvvm_koin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hm6_mvvm_koin.model.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    suspend fun getAllPersons(): List<Person>

    @Query("SELECT * FROM person WHERE idApi BETWEEN 1 AND 20")
    suspend fun getSomePersons(): List<Person>

    @Query("SELECT * FROM person WHERE (:page) LIKE page LIMIT :limit OFFSET :offset")
    suspend fun getSomePersons(limit: Int, offset: Int, page: Int): List<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersons(list: List<Person>)
}