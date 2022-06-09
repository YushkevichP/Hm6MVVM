package com.example.hm6_mvvm_koin

import com.example.hm6_mvvm_koin.database.PersonDao
import com.example.hm6_mvvm_koin.model.Person
import com.example.hm6_mvvm_koin.retrofit.RickMortyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonRepository(
    private val rickMortyApi: RickMortyApi,
    private val rickMortyDao: PersonDao,
) {

    suspend fun fetchPersons(page: Int) = runCatching {
        withContext(Dispatchers.IO) {
            rickMortyApi.getUsersFromApi(page).results
        }
    }

    suspend fun fetchPersonDetails(id: Int) = runCatching {
        withContext(Dispatchers.IO) {
            rickMortyApi.getPersonDetailsFromApi(id)
        }
    }

    suspend fun getSomePersons(limit: Int, offset: Int, page: Int): List<Person> {
        return rickMortyDao.getSomePersons(limit, offset, page)
    }

    suspend fun insertPersons(list: List<Person>) {
        rickMortyDao.insertPersons(list)
    }

}