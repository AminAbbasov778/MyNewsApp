package com.example.mynewsapp.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.Utils.Constants
import com.example.mynewsapp.Utils.Constants.RC_SIGN_IN
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentLoginBinding
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uistates.ValidationState
import com.example.mynewsapp.presentation.viewmodels.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels<LoginViewModel>()
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clearLiveData()
        callbackManager = CallbackManager.Factory.create()
        allButtonClicks()
        observeLiveData()
    }

    private fun allButtonClicks() {
        binding.signuptext.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            clearedittexts()
            viewModel.clearLiveData()
        }
        binding.loginbutton.setOnClickListener {
            loginButtonOperation()
        }
        binding.invisiblepasswordicon.setOnClickListener {
            clickInvisibleIcon()
        }
        binding.visiblepasswordicon.setOnClickListener {
            clickVisibleIcon()
        }
        binding.removeemailtexticon.setOnClickListener {
            displayNormalEmailEdittextDesign()
        }
        binding.removepasswordtexticon.setOnClickListener {
            displayNormalPasswordEdittext()
        }
        binding.googlecardview.setOnClickListener {
            signInWithGoogle()
        }
        binding.facebookcardview.setOnClickListener {
            signInWithFacebook()
        }
    }

    private fun loginButtonOperation() {
        val email = binding.emailedittext.text.toString()
        val password = binding.passwordedittext.text.toString()
        viewModel.getCheckingResultBeforeLogin(email, password)
    }

    private fun observeLiveData() {
        viewModel.validationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ValidationState.Error.EmptyField -> {
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Error.InvalidEmail -> {
                    displayErrorOnEmailEdittext()
                    displayErrorWarning(state.messages)
                }

                is ValidationState.Error.ShortPassword -> {
                    displayNormalEmailEdittextDesign()
                    displayErrorWarning(state.messages)
                    displayErrorOnPasswordEdittext()
                }

                is ValidationState.Success -> {
                    val email = binding.emailedittext.text.toString()
                    val password = binding.passwordedittext.text.toString()
                    viewModel.getLoginResult(email, password)
                }

                else -> {}
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is UiState.Success -> {
                    displaySuccessfulWarning(state.data)
                    checkingCheckbox()
                    displayNormalEmailEdittextDesign()
                    displayNormalPasswordEdittext()
                    binding.loading.setGone()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }

                is UiState.Error -> {
                    displayErrorWarning(state.message)
                    displayErrorOnEmailEdittext()
                    displayErrorOnPasswordEdittext()
                    binding.loading.setGone()
                }
                is UiState.Loading ->  binding.loading.show()

                else -> {}
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

    private fun clickInvisibleIcon() {
        binding.invisiblepasswordicon.setGone()
        binding.visiblepasswordicon.show()
        passwordEdittextVisibleInputType()
    }

    private fun clickVisibleIcon() {
        binding.invisiblepasswordicon.show()
        binding.visiblepasswordicon.setGone()
        passwordEdittextInvisibleInputType()
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

    @SuppressLint("ResourceAsColor")
    private fun displayErrorOnEmailEdittext() {
        binding.emailedittextcardview.setStrokeColor(Color.parseColor("#C30052"))
        val color = ContextCompat.getColor(requireContext(), R.color.error_bg_color)
        binding.passwordedittextcardview.setBackgroundDrawable(ColorDrawable(color))
        binding.removeemailtexticon.show()
    }

    private fun displayNormalEmailEdittextDesign() {
        val color = ContextCompat.getColor(requireContext(), R.color.black)
        binding.emailedittextcardview.setStrokeColor(color)
        binding.passwordedittextcardview.setBackgroundDrawable(ColorDrawable(color))
        binding.removeemailtexticon.setGone()
    }

    private fun checkingCheckbox() {
        val email = binding.emailedittext.text.toString()
        val password = binding.passwordedittext.text.toString()
        val state = binding.remembermecheckbox.isChecked
        viewModel.saveUserLoginInfo(email, password, state)
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
        val color = ContextCompat.getColor(requireContext(), R.color.black)
        binding.passwordedittextcardview.setStrokeColor(color)
        binding.passwordedittextcardview.setBackgroundDrawable(ColorDrawable(color))
    }

    fun clearedittexts() {
        binding.emailedittext.text.clear()
        binding.passwordedittext.text.clear()
    }



    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                result.accessToken?.token?.let { token ->
                    viewModel.signInWithFacebook(token)
                }
            }

            override fun onCancel() {
                displayErrorWarning(R.string.facebook_login_cancelled)
                binding.loading.setGone()
            }

            override fun onError(error: FacebookException) {
                displayErrorWarning(R.string.failure_login)
                binding.loading.setGone()
            }
        })

        binding.loading.show()
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    viewModel.signInWithGoogle(token)
                }
            } catch (e: ApiException) {
                displayErrorWarning(R.string.failure_login)
                binding.loading.setGone()
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
