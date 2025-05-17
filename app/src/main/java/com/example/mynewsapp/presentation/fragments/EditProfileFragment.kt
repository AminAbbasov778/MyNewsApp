package com.example.mynewsapp.presentation.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.Utils.Constants.CAMERA_PERMISSION_REQUEST_CODE
import com.example.mynewsapp.databinding.FragmentEditProfileBinding
import com.example.mynewsapp.presentation.uimodels.profile.ProfileUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.BitmapUtils
import com.example.mynewsapp.presentation.uiutils.CameraPermissionUtils
import com.example.mynewsapp.presentation.uiutils.DialogUtils
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    lateinit var binding : FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels<EditProfileViewModel>()

    private var imageUri: Uri? = null
    private var imageBitmap : Bitmap? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                   binding.profileImg.setImageURI(uri)
                }
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
               binding.profileImg.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickOn()
        observe()
    }


    private fun clickOn() {
        binding.addingImgCardView.setOnClickListener {
            viewModel.getImagePickerOptions()
        }
        binding.saveIcon.setOnClickListener() {
            val username = binding.usernameEditText.text.toString()
            val fullName = binding.fullNameEditText.text.toString()
            val email = binding.yourEmailEditText.text.toString()
            val number = binding.phoneNumberEditText.text.toString()
            val bio = binding.bioEditTExt.text.toString()
            val website = binding.websiteEditTExt.text.toString()
            if(imageUri == null) imageBitmap?.let {imageUri = BitmapUtils.bitmapToUri(requireContext(),it)}

            viewModel.updateUserProfile(imageUri,bio,fullName,email,number,website,username)
        }
        binding.removeIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun observe() {
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            imageUri = uri
            imageUri?.let {
                takePictureLauncher.launch(it)

            }
        }
        viewModel.imagePickerOptions.observe(viewLifecycleOwner) {
            showImagePickerDialog(it)
        }
        viewModel.updatedProfile.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
                is UiState.Loading -> binding.loading.show()
            }

        }
        viewModel.profileData.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> successfulProfileData(it.data)
                is UiState.Error ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
    }

    private fun checkAndRequestCameraPermission() {
        CameraPermissionUtils.checkAndRequestCameraPermission(
            this,
            CAMERA_PERMISSION_REQUEST_CODE,
            onGranted = { viewModel.capturePhoto() },
            onDenied = {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        )

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraPermissionUtils.handlePermissionResult(
            requestCode,
            grantResults,
            CAMERA_PERMISSION_REQUEST_CODE,
            onGranted = { viewModel.capturePhoto() },
            onDenied = {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        )


    }

    private fun showImagePickerDialog(options: Array<String>) {
        DialogUtils.showImagePickerDialog(
            this,
            options = options,
            onCameraClick = { checkAndRequestCameraPermission() },
            onGalleryClick = { pickImageLauncher.launch("image/*") }
        )

    }


    private fun successfulProfileData(profileData : ProfileUiModel){
        binding.loading.setGone()
        imageBitmap = profileData.imageBitmap
        binding.yourEmailEditText.setText(profileData.email)
        binding.fullNameEditText.setText(profileData.fullName)
        binding.websiteEditTExt.setText(profileData.website)
        binding.bioEditTExt.setText(profileData.bio)
        binding.phoneNumberEditText.setText(profileData.phoneNumber)
        binding.profileImg.setImageBitmap(profileData.imageBitmap)
        binding.usernameEditText.setText(profileData.username)
    }
}


