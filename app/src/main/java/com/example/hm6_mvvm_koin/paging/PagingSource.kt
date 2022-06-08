//package com.example.hm6_mvvm_koin.paging
//
//
//import com.example.hm6_mvvm_koin.database.PersonDao
//import com.example.hm6_mvvm_koin.model.CartoonPerson
//import com.example.hm6_mvvm_koin.retrofit.RickMortyApi
//import kotlinx.coroutines.channels.BufferOverflow
//import kotlinx.coroutines.flow.*
//
//class PagingSource(
//    private val rickMortyApi: RickMortyApi,
//    private val personDao: PersonDao,
//) {
//
//    private val loadStateFlow = MutableSharedFlow<LoadState>(
//        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//
//    private var isLoading = false
//    private var currentPage = 0
//
//    fun onLoadMore() {
//        loadStateFlow.tryEmit(LoadState.LOAD_MORE)
//    }
//
//    fun onRefresh() {
//        loadStateFlow.tryEmit(LoadState.REFRESH)
//    }
//
//    fun getData(): Flow<List<CartoonPerson>> {
//        return loadStateFlow
//            .filter { !isLoading }
//            .onEach {
//                if (it == LoadState.REFRESH) {
//                    currentPage = 0
//                }
//                isLoading = true
//            }
//            .map {
//                runCatching { rickMortyApi.getUsers(currentPage).results }
//                    .fold(
//                        onSuccess = { it },
//                        onFailure = { emptyList() })
//            }
//            .onEach {
//                personDao.insertPersons(it)
//                isLoading = false
//                currentPage++
//            }
//            //этот метод для накопления состояния! https://youtu.be/tSyO64FFkTk?t=799
//            .runningReduce { accumulator, value -> accumulator + value }
//            .onStart {
//                emit(personDao.getSomePersons(PAGE_SIZE, 0))
//            }
//
//    }
//
//    enum class LoadState {
//        LOAD_MORE, REFRESH
//    }
//
//    companion object {
//        private const val PAGE_SIZE = 20
//    }
//
//
//}