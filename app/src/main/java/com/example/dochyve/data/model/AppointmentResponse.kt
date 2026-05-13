package com.example.dochyve.data.model

data class AppointmentResponse(
    val status: Int,
    val message: String,
    val data: AppointmentData
)

data class AppointmentData(
    val appointments: List<Appointment>,
    val pagination: Pagination
)

data class Appointment(
    val appointment_id: Int,
    val provider: Provider,
    val patient_id: Int,
    val illness: Illness,
    val date: String,
    val date_formatted: String,
    val time: String,
    val time_formatted: String,
    val datetime_formatted: String,
    val reason: String?,
    val status: String,
    val booking_type: String,
    val share_medical_info: Int,
    val patient: PatientInfo,
    val member: Member?
)

data class Provider(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val practice_name: String,
    val specialization: String,
    val is_out_of_network: Int
)

data class Illness(
    val id: Int,
    val name: String
)

data class PatientInfo(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val date_of_birth: String?,
    val date_of_birth_formatted: String?,
    val gender: String?,
    val address: String?,
    val contact_number: String?,
    val profile_image: String?
)

data class Member(
    val id: Int?,
    val first_name: String?,
    val last_name: String?,
    val relation: String?,
    val image: String?
)

data class Pagination(
    val current_page: Int,
    val total_pages: Int,
    val total_records: Int,
    val per_page: Int,
    val has_more_pages: Boolean
)