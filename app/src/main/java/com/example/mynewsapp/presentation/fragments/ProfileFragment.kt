package com.example.mynewsapp.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentProfileBinding
import com.example.mynewsapp.presentation.adapters.UserNewsAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uimodels.profile.UserProfileUiModel
import com.example.mynewsapp.presentation.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    val viewModel: ProfileViewModel by viewModels<ProfileViewModel>()
   lateinit var  userNewsAdapter : UserNewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        observe()
        onClick()


    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpAdapter(){
        userNewsAdapter = UserNewsAdapter{
               viewModel.deleteNewsByPublishedAt(it)
        }
        binding.userNewsRecView.adapter = userNewsAdapter
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observe() {

        viewModel.profileData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> successfulProfileData(it.data)
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> binding.loading.show()


            }
        }

        viewModel.userNews.observe(viewLifecycleOwner){
            when(it){
                is UiState.Success ->{
                    binding.loading.setGone()
                    binding.newsCount.text = it.data.size.toString()
                    userNewsAdapter.updateList(it.data)
                }
                is UiState.Error -> uiStateObserve(it.message)
                is UiState.Loading ->binding.loading.show()
            }
        }

        viewModel.isNewsDeleted.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> uiStateObserve(it.data)
                is UiState.Error -> uiStateObserve(it.message)
                is UiState.Loading -> binding.loading.show()
            }
        }
        viewModel.followedSourcesCount.observe(viewLifecycleOwner){
            when(it){
                is UiState.Success -> {
                    binding.loading.setGone()
                    binding.followeringCount.text = it.data.toString()
                    binding.followersCount.text = "0"
                }
                is UiState.Error ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClick() {

        binding.settingsIcon.setOnClickListener() {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
        }

        binding.editProfileBtn.setOnClickListener() {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }
        binding.addNewsCardView.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCreateNewsFragment())
        }
        binding.followingConstraint.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFollowingFragment())
        }
        binding.websiteBtn.setOnClickListener {
            val websiteUrl = viewModel.getWebsiteUrl()
            websiteUrl?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                startActivity(intent)
            } ?: run  {
                Toast.makeText(context,R.string.website_is_not_avaliable, Toast.LENGTH_SHORT).show()
            }

        }

    }


    private fun successfulProfileData(data: UserProfileUiModel) {
        binding.loading.setGone()
        binding.fullName.text = data.fullName
        binding.bio.text = data.bio
        binding.profileImg.setImageBitmap(data.imageBitmap)
    }

    private fun uiStateObserve(data : Int){
        binding.loading.setGone()
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
    }
}