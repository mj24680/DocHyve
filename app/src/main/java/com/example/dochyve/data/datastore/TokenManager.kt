package com.example.dochyve.data.datastore

import android.content.Context
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        prefs.edit { putString("token", token) }
    }

    fun getToken() : String?{
        return prefs.getString("token", null)
    }

    fun clearToken(){
        prefs.edit { remove("token") }
    }
}