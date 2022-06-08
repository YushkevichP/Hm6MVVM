package com.example.hm6_mvvm_koin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hm6_mvvm_koin.ItemAdapter
import com.example.hm6_mvvm_koin.R
import com.example.hm6_mvvm_koin.ServiceLocator
import com.example.hm6_mvvm_koin.database.appDataBase
import com.example.hm6_mvvm_koin.databinding.FragmentListBinding
import com.example.hm6_mvvm_koin.model.ItemType
import com.example.hm6_mvvm_koin.utilities.networkChanges
import com.example.hm6_mvvm_koin.viewmodels.ListViewModel
import kotlinx.coroutines.flow.*


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModels<ListViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListViewModel(
                    ServiceLocator.provideRepository(),
                    requireContext().appDataBase.personDao()
                ) as T
            }
        }
    }

    private val personAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ItemAdapter(requireContext()) { item ->
            val personItem = item as? ItemType.Content ?: return@ItemAdapter
            findNavController().navigate(
                ListFragmentDirections.toDetails(personItem.data.idApi)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // для первой подгрузки в список
        viewModel.onLoadMore()

        // запихунть куда-то, чтоб не отобра;алось постаянно / или в тру ничего не делать.
        requireContext().networkChanges
            .onEach {
                when (it) {
                    true -> Toast.makeText(requireContext(), "Працуе канэкшн", Toast.LENGTH_LONG)
                        .show()
                    false -> Toast.makeText(requireContext(),
                        "Не працуе канэкшн",
                        Toast.LENGTH_LONG).show()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        //recucler init
        with(binding) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = personAdapter
            recyclerView.addSpaceDecoration(resources.getDimensionPixelSize(R.dimen.bottom_space))
            swipeLayout.setOnRefreshListener {
                viewModel.onRefresh()
            }
            recyclerView.addPaginationScrollFlow(layoutManager, 1)
                .onEach {
                    viewModel.onLoadMore()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        viewModel.dataFlow
            .onEach {

                personAdapter.submitList(it.map {
                    ItemType.Content(it)
                } + ItemType.Loading)

                binding.swipeLayout.isRefreshing = false
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//        checkNetWorkState()
//        initRecyclerView(layoutManager)
//        loadNewPage(pageCounter)
//        swipeRefresh()


//    private val personRepository by lazy(LazyThreadSafetyMode.NONE) {
//        ServiceLocator.provideRepository()
//    }
//
//    private val personDao by lazy(LazyThreadSafetyMode.NONE) {
//        requireContext().appDataBase.personDao()
//    }

//
//    private var pageCounter = 1
//
//    private var listForSubmitRetrofit: List<ItemType<CartoonPerson>> = emptyList()


//    private fun checkNetWorkState() {
//        requireContext().networkChanges
//            .filter { isWorking ->
//                !isWorking
//            }
//            .onEach {
//                Toast.makeText(requireContext(), " Lost connection", Toast.LENGTH_SHORT).show()
//                uploadCashPerson()
//            }
//            .launchIn(viewLifecycleOwner.lifecycleScope)
//    }
//
//
//    private fun uploadCashPerson() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            // fetch person from db
//            // val listDao = personDao.getFirstTwenty()
//            val listDao = personDao.getAllPersons()
//            println("LIST DAO ==== $listDao")
//            val listForSubmitDao = listDao.map {
//                ItemType.Content(it)
//            }
//            personAdapter.submitList(listForSubmitDao)
//        }
//    }
//
//    private fun loadNewPage(pageForRequest: Int) {
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                // uploadCashPerson()
//                val tempList = personRepository.getUser(pageForRequest)
//                val listPersons = tempList.results
//
//                personDao.insertPersons(listPersons)
//                val content = listPersons.map {
//                    ItemType.Content(it)
//                }
//                val resultList = content.plus(ItemType.Loading)
//                val currentList = personAdapter.currentList.dropLast(1)
//                listForSubmitRetrofit = (currentList + resultList)
//                personAdapter.submitList(listForSubmitRetrofit)
//
//                isLoading = false
//                binding.swipeLayout.isRefreshing = false
//            } catch (e: Throwable) {
//                error(e)
//            }
//        }
//    }
//
//    private fun initRecyclerView(layoutManager: LinearLayoutManager) {
//        with(binding) {
//            recyclerView.apply {
//                addSpaceDecoration(resources.getDimensionPixelSize(R.dimen.bottom_space))
//                adapter = personAdapter
//                recyclerView.layoutManager = layoutManager
//                recyclerView.addPaginationScrollListener(layoutManager, 2) {
//                    if (!isLoading) {
//                        isLoading = true
//                        pageCounter++
//                        loadNewPage(pageCounter)
//                    }
//                }
//            }
//            toolbar.setOnClickListener {
//                refreshListToStart()
//            }
//        }
//    }
//
//    private fun refreshListToStart() {
//        pageCounter = 1
//        listForSubmitRetrofit = emptyList()
//        personAdapter.submitList(listForSubmitRetrofit)
//        loadNewPage(pageCounter)
//
//    }
//
//    private fun swipeRefresh() {
//        binding.swipeLayout.setOnRefreshListener {
//            refreshListToStart()
//        }
//    }

//----tried do with flow
//    private val _paginationFlow = MutableSharedFlow<Unit>()
//    private val paginationFlow = _paginationFlow.asSharedFlow()
//    private fun addScrollListener(layoutManager: LinearLayoutManager) {
//
//        with(binding) {
//            recyclerView
//                .paginationScrollFlow(layoutManager, 4) {
//                    if (!isLoading) {
//                        isLoading = true
//                        loadNewPage(pageCounter)
//                        pageCounter++
//                        println("PAGE COUNTER = $pageCounter")
//                    }
//                }
//                .filter { !isLoading }
//                .onEach { isLoading = true }
//                .map {
//                    personRepository.getUser(pageCounter).results
//                }
//                .map {
//                    it.map {
//                        ItemType.Content(it)
//                    }
//                }
//                .map {
//                    it.plus(ItemType.Loading)
//                }
//                .onEach {
//                    personAdapter.submitList(it)
//                }
//                .onEach {
//                    isLoading = false
//              //      println("PAGE COUNTER = $pageCounter")
//                }
//                .launchIn(viewLifecycleOwner.lifecycleScope)

//            recyclerView.paginationScrollFlow(layoutManager, 1) {
//                _paginationFlow.tryEmit(Unit)
//                if (!isLoading) {
//                    isLoading = true
//                    pageCounter++
//                    loadNewPage(pageCounter)
//                }
//            }
//        }
//    }