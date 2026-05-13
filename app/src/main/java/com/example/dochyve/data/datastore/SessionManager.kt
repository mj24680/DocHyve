package com.example.dochyve.data.datastore

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveEmail(email: String) {
        prefs.edit { putString("email", email) }
    }

    fun getEmail(): String? {
        return prefs.getString("email", null)
    }

    fun clearEmail() {
        prefs.edit { remove("email") }
    }
}