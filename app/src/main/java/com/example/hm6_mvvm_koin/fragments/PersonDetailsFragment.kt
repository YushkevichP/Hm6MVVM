package com.example.hm6_mvvm_koin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.hm6_mvvm_koin.ServiceLocator

import com.example.hm6_mvvm_koin.databinding.FragmentPersonDetailsBinding
import com.example.hm6_mvvm_koin.viewmodels.PersonDetailsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class PersonDetailsFragment : Fragment() {

    private var _binding: FragmentPersonDetailsBinding? = null
    private val binding: FragmentPersonDetailsBinding
        get() = requireNotNull(_binding) {
            "VIEW WAS DESTROYED"
        }

    private val viewModel by viewModels<PersonDetailsViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PersonDetailsViewModel(
                    ServiceLocator.provideRepository(),
                   // requireContext().appDataBase.personDao()
                ) as T
            }
        }
    }

    private val args by navArgs<PersonDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPersonDetailsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupWithNavController(findNavController()) // back_arrow
        val id = args.keyId

        getDetails(id)
    }

    private fun getDetails(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("LOG", "ВЬЮ МОДЕЛЬ ДОЛЖНА ВКЛ")
            viewModel.fetchDetails(id)?.onEach {
                Log.d("LOG", "флоу начинает $it")

                with(binding) {
                    imageUserFragment.load(it.avatarApiDetails)
                    personGender.text = it.gender
                    personName.text = it.name
                    personStatus.text = it.status
                }
            }?.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// можно запускать вот так, одно и то же
//    private fun getDetails(id: Int) = with(binding) {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.fetchDetails(id)
//                ?.onEach { details ->
//                    checkNotNull(details)
//                    imageUserFragment.load(details.avatarApiDetails)
//                    personGender.text = details.gender
//                    personName.text = details.name
//                    personStatus.text = details.status
//                }?.launchIn(viewLifecycleOwner.lifecycleScope)
//        }
//    }