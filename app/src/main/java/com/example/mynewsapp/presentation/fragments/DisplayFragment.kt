package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app.utils.ThemeHelper
import com.example.mynewsapp.databinding.FragmentDisplayBinding
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.viewmodels.DisplayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayFragment : Fragment() {
    lateinit var binding: FragmentDisplayBinding
    val viewModel by viewModels<DisplayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        listener()
    }

    private fun observe() {
        viewModel.isDarkMode.observe(viewLifecycleOwner) {
            it?.let {state ->
                when (state) {
                    is ResultState.Success -> {
                        binding.darkModeSwitch.isChecked = state.data
                        AppCompatDelegate.setDefaultNightMode(
                            if (state.data) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                    is ResultState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun listener() {
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
        }
    }
}
