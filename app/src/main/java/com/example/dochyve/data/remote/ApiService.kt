package com.example.dochyve.data.remote

import com.example.dochyve.data.model.AppointmentResponse
import com.example.dochyve.data.model.LoginResponse
import com.example.dochyve.data.model.MenuResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Parameters

class ApiService(private val client : HttpClient) {

    suspend fun login(email: String, password: String): LoginResponse{
        return client.submitForm (
            url = "login",
            formParameters = Parameters.build {
                append("email", email)
                append("password", password)
                append("user_type", "provider")
            }
        ).body()
    }

    suspend fun getMenuItems(): MenuResponse{
        return client.get("menu-items").body()
    }

    suspend fun getAppointments(page : Int): AppointmentResponse{
        return client.get("provider/appointment-requests"){
            parameter("page", page)
        }.body()
    }

}