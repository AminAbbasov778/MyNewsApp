package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentSettingsBinding
import com.example.mynewsapp.presentation.adapters.SettingsAdapter
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    val viewModel: SettingsViewModel by viewModels<SettingsViewModel>()
    lateinit var settingsAdapter: SettingsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        observe()
        onClick()
    }

    private fun setUpAdapter() {
        settingsAdapter = SettingsAdapter{
            viewModel.onSettingItemClick(it)
        }
        binding.settingsRecView.adapter = settingsAdapter

    }

    private fun observe() {
        viewModel.settingsType.observe(viewLifecycleOwner) {
            settingsAdapter.updateList(it)
        }
        viewModel.isLoggedOut.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())

                }

                is ResultState.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    .show()

            }


        }
        viewModel.navigation.observe(viewLifecycleOwner){
            when(it?.title){
                R.string.language -> {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToLanguageFragment())
                    viewModel.clearData()
                }
                R.string.logout -> viewModel.logOut()
                R.string.display -> {
                    findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDisplayFragment())
                    viewModel.clearData()
                }

            }

        }
    }

    private fun onClick(){
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}