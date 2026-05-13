package com.example.dochyve.data.repository

import com.example.dochyve.R
import com.example.dochyve.data.model.Doctor
import com.example.dochyve.data.model.DrawerMenuItem
import com.example.dochyve.data.model.MenuItem
import com.example.dochyve.data.model.Patient
import com.example.dochyve.data.remote.ApiService

class DashboardRepository(private val apiService: ApiService) {

    suspend fun getMenuItems(): List<DrawerMenuItem> {
        val response = apiService.getMenuItems()

        return mapAPIToDrawerItems(response.data.menu_items)
    }

    private fun mapAPIToDrawerItems(apiItems: List<MenuItem>): List<DrawerMenuItem> {
        return apiItems.map { parent ->
            DrawerMenuItem(
                id = parent.id,
                title = parent.lbl_name,
                iconUrl = parent.icon_alt_full_url,
                route = parent.route,
                isExpandable = parent.is_collapsable == 1,

                children = parent.sub_menu_items.map { child ->
                    DrawerMenuItem(
                        id = child.id,
                        title = child.title,
                        route = child.route,
                        iconUrl = null,
                        isExpandable = false,
                        isChild = true
                    )
                }
            )
        }
    }

    fun getDoctors(): List<Doctor> {
        return listOf(
            Doctor("Emily Johnson", "ENT", "PROVIDER", R.drawable.doctor, true),
            Doctor("Michael Davis", "ENT", "PROVIDER", R.drawable.doctor, false),
            Doctor("Jessica Lee", "Dentist", "PROVIDER", R.drawable.doctor, true),
            Doctor("Robert Brown", "Cardiology", "PROVIDER", R.drawable.doctor, false)
        )
    }

    fun getPatients(): List<Patient> {
        return listOf(
            Patient("Michael Stark", "Male", 32, "Back Pain"),
            Patient("Jason Roy", "Male", 35, "Skin Disorder"),
            Patient("Sarah Carol", "Female", 30, "Cholesterol Issue"),
            Patient("Ben Stoke", "Male", 40, "High Blood Pressure"),
            Patient("Emma Watson", "Female", 28, "Migraine"),
            Patient("Daniel Craig", "Male", 52, "Arthritis"),
            Patient("Olivia Brown", "Female", 45, "Diabetes"),
            Patient("Liam Smith", "Male", 37, "Asthma"),
        )
    }
}
