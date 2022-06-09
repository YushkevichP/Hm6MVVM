package com.example.hm6_mvvm_koin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hm6_mvvm_koin.ItemAdapter
import com.example.hm6_mvvm_koin.PersonRepository
import com.example.hm6_mvvm_koin.R
import com.example.hm6_mvvm_koin.database.AppDatabase
import com.example.hm6_mvvm_koin.databinding.FragmentListBinding
import com.example.hm6_mvvm_koin.model.ItemType
import com.example.hm6_mvvm_koin.utilities.networkChanges
import com.example.hm6_mvvm_koin.viewmodels.ListViewModel
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }
    //denendancy injection
//    private val persontRepository by inject<PersonRepository>()
//    private val appDataBase by inject<AppDatabase>()
    private val viewModel by viewModel<ListViewModel>()

//    private val viewModel by viewModels<ListViewModel> {
//        object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return ListViewModel(
//                    persontRepository,
//                    appDataBase.personDao()
//                ) as T
//            }
//        }
//    }

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

        // запихунть куда-то, чтоб не отображалось постаянно / или в тру ничего не делать.
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
