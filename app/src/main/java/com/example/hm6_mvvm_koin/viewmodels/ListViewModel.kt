package com.example.hm6_mvvm_koin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hm6_mvvm_koin.PersonRepository
import com.example.hm6_mvvm_koin.database.PersonDao

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val personRepository: PersonRepository,
    private val personDao: PersonDao,
) : ViewModel() {

    private var isLoading = false
    private var currentPage = 1
    private val loadMoreFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val dataFlow = loadMoreFlow
        .filter { !isLoading }
        .onEach {
            isLoading = true
            if (it == LoadState.REFRESH) {
                currentPage = 1
                // todo как занулить список если использууем runningReduce?
            }
        }
        .map {
            runCatching {
                delay(1000)
                personRepository.getUser(currentPage).results
            }
                .fold(
                    onSuccess = { it },
                    onFailure = { emptyList() }
                )
        }
        .onEach {
            personDao.insertPersons(it)
            isLoading = false
            currentPage++
        }
        .runningReduce { accumulator, value ->
            accumulator + value
        }
        .onStart {
            emit(personDao.getSomePersons(PAGE_SIZE, 0))
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    fun onLoadMore() {
        loadMoreFlow.tryEmit(LoadState.LOAD_MORE)
    }

    fun onRefresh() {
        loadMoreFlow.tryEmit(LoadState.REFRESH)
    }

    companion object {
        private const val PAGE_SIZE = 1
    }

    enum class LoadState {
        LOAD_MORE, REFRESH
    }
}