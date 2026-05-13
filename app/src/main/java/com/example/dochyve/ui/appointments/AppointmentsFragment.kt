package com.example.dochyve.ui.appointments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dochyve.R
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.remote.ApiService
import com.example.dochyve.data.remote.HttpClientFactory
import com.example.dochyve.data.repository.AppointmentRepository
import com.example.dochyve.databinding.FragmentAppointmentsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppointmentsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentsBinding
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var adapter: AppointmentPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewmodel()
        setupRecyclerView()
        setupSwipeRefresh()
        observeAppointments()
        observeLoadState()
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {

            adapter.loadStateFlow.collectLatest { loadState ->

                // show/hide swipe refresh loader
                binding.swipeRefresh.isRefreshing =
                    loadState.refresh is LoadState.Loading
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            // tell paging to reload fresh data
            adapter.refresh()
        }
    }

    private fun setupViewmodel() {
        val tokenManager = TokenManager(requireContext())
        val apiService = ApiService(HttpClientFactory.create(tokenManager))
        val repository = AppointmentRepository(apiService)
        viewModel = AppointmentViewModel(repository)
    }

    private fun observeAppointments() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.appointments.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = AppointmentPagingAdapter(
            onAcceptClick = { appointment ->
                Toast.makeText(
                    requireContext(),
                    "Accepted: ${appointment.appointment_id}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRejectClick = { appointment ->
                Toast.makeText(
                    requireContext(),
                    "Rejected: ${appointment.appointment_id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.rvAppointments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AppointmentsFragment.adapter
        }
    }
}