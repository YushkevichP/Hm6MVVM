package com.example.hm6_mvvm_koin.koin

import com.example.hm6_mvvm_koin.viewmodels.ListViewModel
import com.example.hm6_mvvm_koin.viewmodels.PersonDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ListViewModel(get())
    }

    viewModel {
       PersonDetailsViewModel(get())
    }
}