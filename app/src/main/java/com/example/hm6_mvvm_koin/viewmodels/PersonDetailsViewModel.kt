package com.example.hm6_mvvm_koin.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hm6_mvvm_koin.PersonRepository
import com.example.hm6_mvvm_koin.database.PersonDao
import com.example.hm6_mvvm_koin.model.PersonDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PersonDetailsViewModel(
    private val personRepository: PersonRepository,
    private val personDao: PersonDao,
) : ViewModel() {

    suspend fun fetchDetails(id: Int): Flow<PersonDetails>? {
        return personRepository.fetchPersonDetails(id)
            .map {
                flowOf(it)
            }
            .getOrNull()
    }
}