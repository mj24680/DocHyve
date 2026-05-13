package com.example.dochyve.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.remote.ApiService
import com.example.dochyve.data.remote.HttpClientFactory
import com.example.dochyve.data.repository.DashboardRepository
import com.example.dochyve.databinding.FragmentDashboardBinding
import com.example.dochyve.ui.dashboard.adapters.DatePickerAdapter
import com.example.dochyve.ui.dashboard.adapters.DoctorAdapter
import com.example.dochyve.ui.dashboard.adapters.PatientAdapter
import com.example.dochyve.ui.patient_appointments.PatientAppointmentsActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardFragmentViewModel

    // adapter
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var dateAdapter: DatePickerAdapter
    private lateinit var patientAdapter: PatientAdapter

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapters()
        setupRecyclerViews()
        setupClicks()
        observeViewModel()

        loadData()
    }

    private fun setupViewModel() {
        tokenManager = TokenManager(requireContext())
        val apiService = ApiService(HttpClientFactory.create(tokenManager))
        val repository = DashboardRepository(apiService)
        viewModel = ViewModelProvider(
            this,
            DashboardFragmentViewModelFactory(repository)
        )[DashboardFragmentViewModel::class.java]
    }

    private fun setupAdapters() {
        doctorAdapter = DoctorAdapter()
        patientAdapter = PatientAdapter()

        dateAdapter = DatePickerAdapter { selectedDate ->
            viewModel.onDateSelected(selectedDate.fullDate)
        }
    }

    private fun setupRecyclerViews() {

        binding.rvDoctors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = doctorAdapter
        }

        binding.rvPatients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = patientAdapter
        }

        binding.rvDates.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dateAdapter
        }
    }

    private fun setupClicks() {

        binding.btnNext.setOnClickListener {
            viewModel.onNextWeek()
        }

        binding.btnPrev.setOnClickListener {
            viewModel.onPreviousWeek()
        }

        binding.btnViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), PatientAppointmentsActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.loadDashboardData()
    }

    private fun observeViewModel() {

        viewModel.doctors.observe(viewLifecycleOwner) {
            doctorAdapter.submitList(it)
        }

        viewModel.patients.observe(viewLifecycleOwner) {
            patientAdapter.submitList(it)
        }

        viewModel.weekDates.observe(viewLifecycleOwner) { dates ->
            dateAdapter.submitList(dates)

            val currentSelected = viewModel.selectedDate.value
            val index = dates.indexOfFirst { it.fullDate == currentSelected }

            if (index != -1) {
                dateAdapter.setSelectedPosition(index)
            }
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) {
            updateMonthYearText(it)
        }

        viewModel.canGoBack.observe(viewLifecycleOwner) { canGoBack ->
            binding.btnPrev.isEnabled = canGoBack
            binding.btnPrev.alpha = if (canGoBack) 1f else 0.3f
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }

    private fun updateMonthYearText(date: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("MMMM - yyyy")
        binding.txtMonthYear.text = date.format(formatter)
    }
}
