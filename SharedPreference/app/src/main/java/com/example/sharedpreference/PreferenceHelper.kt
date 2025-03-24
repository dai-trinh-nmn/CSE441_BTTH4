package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper (context: Context){
    private val prefs: SharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun saveCredentials(username: String, password: String){
        prefs.edit().putString("username", username).apply()
        prefs.edit().putString("password", password).apply()
    }

    fun getUsername(): String? {
        return prefs.getString("username", null)
    }

    fun getPassword(): String? {
        return prefs.getString("password", null)
    }

    fun clearCredentials(){
        prefs.edit().clear().apply()
    }
}
