package com.example.mynewsapp.data.repositories

import android.content.SharedPreferences
import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import javax.inject.Inject
import javax.inject.Named

class SharedPreferenceRepositoryImpl @Inject constructor(
    @Named("encrypted") val encryptedSharedPreference: SharedPreferences,
    @Named("default") val sharedPreferences: SharedPreferences,
) :
    SharedPreferenceRepository {


    override fun saveUserLoginInfo(email: String, password: String) {
        var editor = encryptedSharedPreference.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()

    }

    override fun removeUserLoginInfo(): Result<Boolean> {
        return try {
            val editor = encryptedSharedPreference.edit()
            editor.clear()
            editor.apply()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }


    }

    override fun getUserEmail(): Result<String> {
        return try {
            val email = encryptedSharedPreference.getString("email", "")
            Result.success(email!!)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun markFirstLaunchDone(isFirstLaunchOver: Boolean) =
        sharedPreferences.edit().putBoolean("isFirstLaunch", isFirstLaunchOver).apply()


    override fun isFirstLaunch(): Boolean = sharedPreferences.getBoolean("isFirstLaunch", true)


}