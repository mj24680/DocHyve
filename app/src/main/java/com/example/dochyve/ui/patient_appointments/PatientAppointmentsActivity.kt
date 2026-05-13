package com.example.dochyve.ui.patient_appointments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dochyve.R
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.remote.ApiService
import com.example.dochyve.data.remote.HttpClientFactory
import com.example.dochyve.data.repository.DashboardRepository
import com.example.dochyve.databinding.ActivityPatientAppointmentsBinding
import com.example.dochyve.ui.dashboard.adapters.PatientAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientAppointmentsBinding
    private lateinit var viewModel: PatientAppointmentsViewModel
    private lateinit var patientAdapter: PatientAdapter
    private lateinit var tokenManager: TokenManager
    
    private val statusList = listOf("All", "Scheduled", "Confirmed", "Pending")
    private var isFirstSelection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupToolbar()
        setupSpinner()
        setupRecyclerView()
        observeViewModel()

        binding.etDateRange.setOnClickListener {
            openDateRangePicker()
        }
    }

    private fun setupViewModel() {
        tokenManager = TokenManager(this)
        val apiService = ApiService(HttpClientFactory.create(tokenManager))
        val repository = DashboardRepository(apiService)
        viewModel = PatientAppointmentsViewModel(repository)
    }

    private fun setupToolbar() {
        binding.appbarPA.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        patientAdapter = PatientAdapter()
        binding.rvAllPatientAppointments.apply {
            layoutManager = LinearLayoutManager(this@PatientAppointmentsActivity)
            adapter = patientAdapter
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerStatus.adapter = adapter
        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                viewModel.onStatusSelected(statusList[position])
                showMessage("showing ${statusList[position]} Items")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun observeViewModel() {
        viewModel.patients.observe(this) { patients ->
            patientAdapter.submitList(patients)
        }

        viewModel.selectedDateRange.observe(this) { range ->
            binding.etDateRange.setText(range)
        }
    }

    private fun openDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(R.style.Theme_DocHyve_DatePicker)
            .setTitleText("SELECT DATE RANGE")
            .build()

        dateRangePicker.show(supportFragmentManager, "DATE_RANGE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            if (startDate != null && endDate != null) {
                val formattedRange = "${formatDate(startDate)} - ${formatDate(endDate)}"
                viewModel.onDateRangeSelected(formattedRange)
            }
        }
    }

    private fun formatDate(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timeInMillis))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
