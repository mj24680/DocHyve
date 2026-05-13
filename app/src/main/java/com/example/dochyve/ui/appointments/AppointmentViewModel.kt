package com.example.dochyve.ui.appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.dochyve.data.repository.AppointmentRepository

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() {

    val appointments = repository.getAppointments().cachedIn(viewModelScope)

}