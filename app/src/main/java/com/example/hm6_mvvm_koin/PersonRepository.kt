package com.example.hm6_mvvm_koin

import com.example.hm6_mvvm_koin.retrofit.RickMortyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonRepository(private val rickMortyApi:RickMortyApi) {

    suspend fun getUser(page: Int) = withContext(Dispatchers.IO){
       // delay(2000)
       rickMortyApi.getUsers(page)
    }

    suspend fun getPersonDetails(id: Int) = withContext(Dispatchers.IO){
        rickMortyApi.getUserDetails(id)
    }


}