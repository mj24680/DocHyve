package com.example.dochyve.data.repository

import com.example.dochyve.data.datastore.SessionManager
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.model.LoginResponse
import com.example.dochyve.data.remote.ApiService

class AuthRepository(private val apiService: ApiService, private val tokenManager: TokenManager, private val sessionManager: SessionManager) {

    suspend fun login(email: String, password: String) : Result<LoginResponse>{
        return try {
            val response = apiService.login(email, password)

            if(response.status == 200){
                val token = response.data.authorization.token
                tokenManager.saveToken(token)
                sessionManager.saveEmail(email)
                Result.success(response)
            }else{
                Result.failure(
                    Exception(response.message)
                )
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}