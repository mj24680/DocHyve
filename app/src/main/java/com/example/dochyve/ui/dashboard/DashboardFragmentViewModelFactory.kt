package com.example.dochyve.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dochyve.data.repository.DashboardRepository

class DashboardFragmentViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardFragmentViewModel(repository) as T
    }
}