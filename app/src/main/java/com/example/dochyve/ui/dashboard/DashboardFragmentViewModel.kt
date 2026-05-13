package com.example.dochyve.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dochyve.data.model.Date
import com.example.dochyve.data.model.Doctor
import com.example.dochyve.data.model.Patient
import com.example.dochyve.data.repository.DashboardRepository
import java.time.LocalDate

class DashboardFragmentViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _doctors = MutableLiveData<List<Doctor>>()
    val doctors: LiveData<List<Doctor>> = _doctors

    private val _patients = MutableLiveData<List<Patient>>()
    val patients: LiveData<List<Patient>> = _patients

    private val _weekDates = MutableLiveData<List<Date>>()
    val weekDates: LiveData<List<Date>> = _weekDates

    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private val _canGoBack = MutableLiveData<Boolean>()
    val canGoBack: LiveData<Boolean> = _canGoBack

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentWeekStartDate: LocalDate = LocalDate.now()
    private val todayDate: LocalDate = LocalDate.now()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        _isLoading.value = true

        _doctors.value = repository.getDoctors()
        _patients.value = repository.getPatients().take(4)
        generateWeekDates(currentWeekStartDate)

        _isLoading.value = false
    }

    private fun generateWeekDates(startDate: LocalDate) {
        val dates = List(7) { index ->
            val date = startDate.plusDays(index.toLong())
            Date(
                dayName = date.dayOfWeek.name.take(3),
                dayNumber = date.dayOfMonth.toString(),
                fullDate = date
            )
        }
        _weekDates.value = dates
        
        // Select today if it's in this week, otherwise select first day of week
        val todayInWeek = dates.find { it.fullDate == todayDate }
        _selectedDate.value = todayInWeek?.fullDate ?: dates[0].fullDate
        
        _canGoBack.value = currentWeekStartDate.isAfter(todayDate)
    }

    fun onNextWeek() {
        currentWeekStartDate = currentWeekStartDate.plusDays(7)
        generateWeekDates(currentWeekStartDate)
    }

    fun onPreviousWeek() {
        if (currentWeekStartDate.isAfter(todayDate)) {
            currentWeekStartDate = currentWeekStartDate.minusDays(7)
            generateWeekDates(currentWeekStartDate)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}
