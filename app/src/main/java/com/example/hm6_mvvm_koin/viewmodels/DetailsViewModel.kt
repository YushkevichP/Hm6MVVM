package com.example.hm6_mvvm_koin.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hm6_mvvm_koin.PersonRepository
import com.example.hm6_mvvm_koin.database.PersonDao
import com.example.hm6_mvvm_koin.fragments.PersonDetailsFragment

class DetailsViewModel(
    private val personRepository: PersonRepository,
    private val personDao: PersonDao,
   private val id: Int
) : ViewModel() {

}