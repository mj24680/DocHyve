package com.example.dochyve.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.dochyve.R
import com.example.dochyve.data.model.Appointment
import com.example.dochyve.data.paging.AppointmentPagingSource
import com.example.dochyve.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class AppointmentRepository(private val apiService: ApiService) {


    // instead of loading all appointments at once, this function loads data page by page automatically when user scrolls
    fun getAppointments(): Flow<PagingData<Appointment>> { // paging data -> I know how to load items gradually when needed, it contains paging instructions internally, adapter understands this format
        return Pager( // it is a manager, pager knows -> when to load, how much to load, next & previous page etc.
            config = PagingConfig(
                pageSize = 10, // load 10 items per request
                enablePlaceholders = false // if true -> fake empty rows at the end
            ),

            pagingSourceFactory = { // this tells pager -> whenever data is needed use 'AppointmentPagingSource' class
                AppointmentPagingSource(apiService)
            }
        ).flow
    }
}