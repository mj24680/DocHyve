package com.example.dochyve.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dochyve.data.model.Appointment
import com.example.dochyve.data.remote.ApiService

// if pager is manager then AppointmentPagingSource is worker that -> calls API, loads pages, tells paging -> what came before, what comes next and when paging ends
class AppointmentPagingSource(private val apiService: ApiService) : // one main job -> given a page number, fetch data from API, and return paging information
    PagingSource<Int, Appointment>() { // PagingSource has two types, in this case INT = page number & Appointment = item type

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Appointment> {
        return try {
            // first page = 1
            val currentPage = params.key ?: 1 // pager sends page number inside params.key

            // API call
            val response = apiService.getAppointments(currentPage)

            val appointments = response.data.appointments

            // chk next page exists or not
            val nextPage = if (response.data.pagination.has_more_pages) {
                currentPage + 1
            } else {
                null
            }

            LoadResult.Page( // this is success response it contains -> data, previous page, next page
                data = appointments,

                // previous page
                prevKey = if (currentPage == 1) null
                else currentPage - 1,

                // next page
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    // this function job is -> when refresh happens, which page should reload first
    override fun getRefreshKey(state: PagingState<Int, Appointment>): Int? { // state holds current snapshot of paging system like loaded pages, loaded items, scroll position, previous & next keys
        return state.anchorPosition?.let { position -> // anchorPosition -> current visible item position
            val page = state.closestPageToPosition(position) // find the loaded page nearest to this item position, page only contains prev and next key

            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}