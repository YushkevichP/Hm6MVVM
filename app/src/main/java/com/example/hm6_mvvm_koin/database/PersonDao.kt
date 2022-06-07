package com.example.hm6_mvvm_koin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hm6_mvvm_koin.model.CartoonPerson

@Dao
interface PersonDao {
    @Query("SELECT * FROM cartoonperson")
    suspend fun getAllPersons(): List<CartoonPerson>

//    @Query("SELECT * FROM cartoonperson WHERE idApi BETWEEN 1 AND 20")
//    suspend fun getSomePersons(): List<CartoonPerson>

    @Query("SELECT * FROM cartoonperson LIMIT :limit OFFSET :offset")
    suspend fun getSomePersons(limit: Int, offset: Int): List<CartoonPerson>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersons(list: List<CartoonPerson>)
}