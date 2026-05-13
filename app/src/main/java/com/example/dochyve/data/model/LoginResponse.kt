package com.example.dochyve.data.model

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val user: User,
    val authorization: Authorization
)

data class Authorization(
    val token: String
)

data class User(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val email: String,
    val user_type: String
)
