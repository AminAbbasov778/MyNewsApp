package com.example.mynewsapp.presentation.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mynewsapp.R
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.ActivityMainBinding
import com.example.mynewsapp.presentation.uistates.ResultState
import com.example.mynewsapp.presentation.viewmodels.MainViewModel
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment : NavHostFragment
    val viewModel : MainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        setContentView(binding.root)
        setupBottomNavigation()
        bottomNavigationVisiblityHandler()
        observe()
        binding.bottomnavigation.itemIconTintList = null


    }

       fun setupBottomNavigation() {
             navHostFragment =supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomnavigation,navHostFragment.navController)
    }

    fun bottomNavigationVisiblityHandler(){
        navHostFragment.navController.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.detailFragment -> binding.bottomnavigation.setGone()
                R.id.homeFragment -> binding.bottomnavigation.show()
                R.id.onBoardingFragment -> binding.bottomnavigation.setGone()
                R.id.bookmarkFragment -> binding.bottomnavigation.show()
                R.id.loginFragment -> binding.bottomnavigation.setGone()
                R.id.signupFragment ->binding.bottomnavigation.setGone()
                R.id.profileFragment ->binding.bottomnavigation.show()
                R.id.searchFragment -> binding.bottomnavigation.setGone()
                R.id.createNewsFragment -> binding.bottomnavigation.setGone()
                R.id.editProfileFragment -> binding.bottomnavigation.setGone()
                R.id.settingsFragment -> binding.bottomnavigation.setGone()
            }
        }


    }

    fun observe(){

        viewModel.isFirstLaunch.observe(this){
            if(it){
                Log.e("yoxla1",it.toString(), )
                findNavController(R.id.fragmentContainerView).navigate(R.id.onBoardingFragment)
            }
            else{
                Log.e("yoxla2",it.toString(), )
                viewModel.checkUserInfo()
            }
        }
     viewModel.isUserLoggedIn.observe(this){
             when(it){
                 is ResultState.Success ->{
                     if(it.data){
                         Log.e("yoxla3",it.toString(), )
                         findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)

                     }
                     else{
                         Log.e("yoxla6",it.toString(), )
                         findNavController(R.id.fragmentContainerView).navigate(R.id.loginFragment)
                     }
                 }

                 is ResultState.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
             }



     }

    }


}