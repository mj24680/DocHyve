package com.example.dochyve.ui.patient_appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dochyve.data.model.Patient
import com.example.dochyve.data.repository.DashboardRepository

class PatientAppointmentsViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _patients = MutableLiveData<List<Patient>>()
    val patients: LiveData<List<Patient>> = _patients

    private val _selectedDateRange = MutableLiveData<String>()
    val selectedDateRange: LiveData<String> = _selectedDateRange

    private val _selectedStatus = MutableLiveData<String>()
    val selectedStatus: LiveData<String> = _selectedStatus

    init {
        loadPatients()
    }

    private fun loadPatients() {
        _patients.value = repository.getPatients()
    }

    fun onDateRangeSelected(range: String) {
        _selectedDateRange.value = range
    }

    fun onStatusSelected(status: String) {
        _selectedStatus.value = status
    }
}
