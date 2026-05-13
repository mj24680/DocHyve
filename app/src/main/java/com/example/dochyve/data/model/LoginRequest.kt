package com.example.dochyve.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val user_type: String = "provider"
)
