package com.example.dochyve.data.model

data class Patient(
    val name: String,
    val gender: String,
    val age: Int,
    val issue: String,
    val imageUrl: String? = null
)
