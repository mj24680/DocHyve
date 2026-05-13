package com.example.dochyve.data.model

data class MenuResponse (
    val status: Int,
    val message: String,
    val data: MenuData
)

data class MenuData(
    val menu_items: List<MenuItem>
)

data class MenuItem(
    val id: Int,
    val sequence: Int,
    val lbl_name: String,
    val is_collapsable: Int,
    val icon_alt_full_url: String?,
    val route: String?,
    val sub_menu_items: List<SubMenuItem>
)
data class SubMenuItem(
    val id: Int,
    val title: String,
    val route: String?
)