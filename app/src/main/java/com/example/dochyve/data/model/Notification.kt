package com.example.dochyve.data.model

data class Notification (
    val id: Int,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val duration: String,
    val imageUrl: Int? = null,
)