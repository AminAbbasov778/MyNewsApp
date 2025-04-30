package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentOnBoardingBinding
import com.example.mynewsapp.presentation.adapters.OnBoardingAdapter
import com.example.mynewsapp.presentation.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {
    lateinit var binding: FragmentOnBoardingBinding
    val viewModel: OnBoardingViewModel by viewModels<OnBoardingViewModel>()
    lateinit var boardingAdapter: OnBoardingAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        onClick()
        observe()


    }


    private fun onClick() {
        binding.backButton.setOnClickListener() {
            var currentItem = binding.viewPager.currentItem
            binding.viewPager.currentItem = currentItem - 1
        }


        binding.nextbutton.setOnClickListener() {
            var currentItem = binding.viewPager.currentItem
            var boardingList = viewModel.boardingUiModelList.value
            if (currentItem < boardingList!!.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                viewModel.markFirstLaunchDone(false)
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
            }
        }


    }

    private fun observe() {
        viewModel.boardingUiModelList.observe(viewLifecycleOwner) {
            boardingAdapter.updateList(it)
        }

    }

    private fun setUpViewPager() {
        boardingAdapter = OnBoardingAdapter()
        binding.viewPager.adapter = boardingAdapter



        binding.dotsIndicator.setViewPager2(binding.viewPager)


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (binding.viewPager.currentItem != 0) {
                    binding.backButton.show()
                } else {
                    binding.backButton.setGone()
                }


            }
        })


    }


}