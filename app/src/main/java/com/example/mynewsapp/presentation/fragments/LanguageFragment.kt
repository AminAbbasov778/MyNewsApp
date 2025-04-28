package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentLanguageBinding
import com.example.mynewsapp.presentation.adapters.LanguageAdapter
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.viewmodels.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : Fragment() {
   lateinit var binding : FragmentLanguageBinding
   val viewModel by viewModels<LanguageViewModel>()
   var languageAdapter : LanguageAdapter? =null

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
       binding = FragmentLanguageBinding.inflate(inflater,container,false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setupAdapter()
      observe()
      onClick()
   }

   private fun setupAdapter(){
      languageAdapter = LanguageAdapter{viewModel.setLanguage(it)}
      binding.languageRecView.adapter = languageAdapter
   }

   private fun observe(){
      viewModel.languageList.observe(viewLifecycleOwner){
         when(it){
            is ResultState.Success -> languageAdapter?.updateList(it.data)
            is ResultState.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
         }

      }

   }
   private fun onClick(){
      binding.backIcon.setOnClickListener {
         findNavController().popBackStack()
      }
   }
}