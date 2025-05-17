package com.example.mynewsapp.presentation.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.Utils.Constants.CAMERA_PERMISSION_REQUEST_CODE
import com.example.mynewsapp.databinding.FragmentCreateNewsBinding
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.CameraPermissionUtils
import com.example.mynewsapp.presentation.uiutils.DialogUtils
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.CreateNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateNewsFragment : Fragment() {
    lateinit var binding : FragmentCreateNewsBinding
    val viewModel by viewModels<CreateNewsViewModel>()
    private var imageUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    viewModel.onImageChanged(uri.toString())
                    binding.addCoverPhoto.setImageURI(uri)
                }
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                viewModel.onImageChanged(imageUri.toString())
                binding.addCoverPhoto.setImageURI(it)
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        observe()
        collect()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClick(){
        binding.addCoverPhoto.setOnClickListener {
           viewModel.getImagePickerOptions()
        }
        binding.newsTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onTitleChanged(text.toString())
        }

        binding.addArticleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text.toString())
        }
        binding.publishBtn.setOnClickListener {
            viewModel.togglePublishedBtn()
        }
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observe(){
        viewModel.imagePickerOptions.observe(viewLifecycleOwner) {
            showImagePickerDialog(it)

        }
        viewModel.imageUri.observe(viewLifecycleOwner) {
            it?.let {
                takePictureLauncher.launch(it)
            }
        }
        viewModel.createNewsResult.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> {
                    binding.loading.setGone()
                    findNavController().popBackStack()
                }
                is UiState.Error ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
        viewModel.isPublishedActive.observe(viewLifecycleOwner){
            if(it){
                val title = binding.newsTitleEditText.text.toString()
                val article = binding.addArticleEditText.text.toString()
                viewModel.createNews(title,article,imageUri!!)
            }else{
                Toast.makeText(context, R.string.empty_fields_message, Toast.LENGTH_SHORT).show()

            }
        }

    }
    private fun collect(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFormValid.collect { isValid ->
                    if(isValid){
                        binding.publishBtn.apply {
                            isEnabled = true
                            setBackgroundColor(resources.getColor(R.color.primary_blue))
                            setTextColor(resources.getColor(R.color.white))
                        }


                    }else{
                        binding.publishBtn.apply {
                            isEnabled = false
                            setBackgroundColor(resources.getColor(R.color.second_btn_color))
                            setTextColor(resources.getColor(R.color.second_text_color))

                        }
                    }


                }
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

}