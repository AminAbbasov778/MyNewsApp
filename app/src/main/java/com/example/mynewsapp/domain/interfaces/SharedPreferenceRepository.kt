package com.example.mynewsapp.domain.interfaces

interface SharedPreferenceRepository {


    fun saveUserLoginInfo(email: String, password: String)
    fun removeUserLoginInfo(): Result<Boolean>
    fun getUserEmail(): Result<String>
    fun markFirstLaunchDone(isFirstLaunchOver: Boolean)
    fun isFirstLaunch(): Boolean

}
