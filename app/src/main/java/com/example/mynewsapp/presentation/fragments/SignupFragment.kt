package com.example.mynewsapp.presentation.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentSignupBinding
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uistates.ValidationState
import com.example.mynewsapp.presentation.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    val viewModel by viewModels<SignupViewModel>()
    var email: String? = null
    var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allButtonsClicks()
        observe()
    }

    private fun allButtonsClicks() {
        binding.logintext.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signupbutton.setOnClickListener {
            signupProcess()
        }

        binding.removeemailtexticon.setOnClickListener {
            displayNormalEmailEdittextDesign()
        }

        binding.removepasswordtexticon.setOnClickListener {
            displayNormalPasswordEdittext()
        }

        binding.removeconfirmpasswordicon.setOnClickListener {
            displayNormalConfirmPasswordEdittext()
        }

        binding.invisiblepasswordicon.setOnClickListener {
            clickInvisiblePasswordIcon()
        }

        binding.visiblepasswordicon.setOnClickListener {
            clickVisiblePasswordIcon()
        }

        binding.invisibleconfirmpasswordicon.setOnClickListener {
            clickInvisibleConfirmPasswordIcon()
        }

        binding.visibleconfirmpasswordicon.setOnClickListener {
            clickVisibleConfirmPasswordIcon()
        }
    }

    private fun signupProcess() {
        email = binding.emailedittext.text.toString()
        password = binding.passwordedittext.text.toString()
        val confirmedPassword = binding.confirmpasswordedittext.text.toString()
        viewModel.prePasswordValidation(email!!, password!!, confirmedPassword)
    }

    private fun observe() {
        viewModel.validationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ValidationState.Error.EmptyField -> {
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Error.InvalidEmail -> {
                    displayErrorOnEmailEdittext()
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Error.UnsafetyPassword -> {
                    displayNormalEmailEdittextDesign()
                    displayNormalConfirmPasswordEdittext()
                    displayErrorOnPasswordEdittext()
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Error.ConfirmPasswordDismatch -> {
                    displayNormalEmailEdittextDesign()
                    displayNormalPasswordEdittext()
                    displayErrorOnConfirmPasswordEdittext()
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Success -> {
                    displaySuccessfulWarning(state.messages)
                    displayNormalConfirmPasswordEdittext()
                    displayNormalPasswordEdittext()
                    displayNormalEmailEdittextDesign()
                    viewModel.getSignupResult(email!!, password!!)
                }

                else -> {}
            }
        }

        viewModel.signupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.loading.show()
                is UiState.Success -> {
                    binding.loading.setGone()
                    displaySuccessfulWarning(state.data)
                    findNavController().popBackStack()
                }
                is UiState.Error -> {
                    binding.loading.setGone()
                    displayErrorWarning(state.message)
                }
            }
        }
    }

    private fun displayErrorWarning(warning: Int) {
        binding.warningconstraint.show()
        binding.warningicon.show()
        binding.successwarningicon.setGone()
        binding.warningtext.setTextColor(Color.parseColor("#C30052"))
        binding.warningtext.text = getString(warning)
    }

    private fun displaySuccessfulWarning(warning: Int) {
        binding.warningconstraint.show()
        binding.warningicon.setGone()
        binding.successwarningicon.show()
        binding.warningtext.setTextColor(Color.parseColor("#4CAF50"))
        binding.warningtext.text = getString(warning)
    }

    private fun displayErrorOnPasswordEdittext() {
        binding.invisiblepasswordicon.setGone()
        binding.visiblepasswordicon.setGone()
        binding.removepasswordtexticon.show()
        binding.passwordedittextcardview.setStrokeColor(Color.parseColor("#C30052"))
        val color = ContextCompat.getColor(requireContext(), R.color.error_bg_color)
        binding.passwordedittextcardview.setBackgroundDrawable(ColorDrawable(color))
    }

    private fun displayNormalPasswordEdittext() {
        binding.removepasswordtexticon.setGone()
        binding.invisiblepasswordicon.show()
        binding.visiblepasswordicon.setGone()
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)
        binding.passwordedittextcardview.setStrokeColor(blackColor)
        binding.passwordedittextcardview.setBackgroundDrawable(ColorDrawable(color))
    }

    private fun displayErrorOnConfirmPasswordEdittext() {
        binding.invisibleconfirmpasswordicon.setGone()
        binding.visibleconfirmpasswordicon.setGone()
        binding.removeconfirmpasswordicon.show()
        binding.confirmpasswordedittextcardview.setStrokeColor(Color.parseColor("#C30052"))
        val color = ContextCompat.getColor(requireContext(), R.color.error_bg_color)
        binding.confirmpasswordedittextcardview.setBackgroundDrawable(ColorDrawable(color))

    }

    private fun displayNormalConfirmPasswordEdittext() {
        binding.removeconfirmpasswordicon.setGone()
        binding.invisibleconfirmpasswordicon.show()
        binding.visibleconfirmpasswordicon.setGone()
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)
        binding.confirmpasswordedittextcardview.setStrokeColor(blackColor)
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        binding.confirmpasswordedittextcardview.setBackgroundDrawable(ColorDrawable(color))

    }

    private fun displayErrorOnEmailEdittext() {
        binding.emailedittextcardview.setStrokeColor(Color.parseColor("#C30052"))
        val color = ContextCompat.getColor(requireContext(), R.color.error_bg_color)
        binding.emailedittextcardview.setBackgroundDrawable(ColorDrawable(color))
        binding.removeemailtexticon.show()
    }

    private fun displayNormalEmailEdittextDesign() {
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)
        binding.emailedittextcardview.setStrokeColor(blackColor)
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        binding.emailedittextcardview.setBackgroundDrawable(ColorDrawable(color))
        binding.removeemailtexticon.setGone()
    }

    private fun clickInvisiblePasswordIcon() {
        binding.invisiblepasswordicon.setGone()
        binding.visiblepasswordicon.show()
        passwordEdittextVisibleInputType()
    }

    private fun clickInvisibleConfirmPasswordIcon() {
        binding.invisibleconfirmpasswordicon.setGone()
        binding.visibleconfirmpasswordicon.show()
        confirmPasswordEdittextVisibleInputType()
    }

    private fun clickVisiblePasswordIcon() {
        binding.invisiblepasswordicon.show()
        binding.visiblepasswordicon.setGone()
        passwordEdittextInvisibleInputType()
    }

    private fun clickVisibleConfirmPasswordIcon() {
        binding.invisibleconfirmpasswordicon.show()
        binding.visibleconfirmpasswordicon.setGone()
        confirmPasswordEdittextInvisibleInputType()
    }

    private fun passwordEdittextInvisibleInputType() {
        binding.passwordedittext.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.passwordedittext.setSelection(binding.passwordedittext.text.length)
    }

    private fun passwordEdittextVisibleInputType() {
        binding.passwordedittext.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.passwordedittext.setSelection(binding.passwordedittext.text.length)
    }

    private fun confirmPasswordEdittextInvisibleInputType() {
        binding.confirmpasswordedittext.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.confirmpasswordedittext.setSelection(binding.confirmpasswordedittext.text.length)
    }

    private fun confirmPasswordEdittextVisibleInputType() {
        binding.confirmpasswordedittext.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.confirmpasswordedittext.setSelection(binding.confirmpasswordedittext.text.length)
    }
}