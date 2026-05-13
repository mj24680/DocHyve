package com.example.dochyve.data.model

data class DrawerMenuItem (
    val id: Int,
    val title: String,
    val iconUrl: String? = null,
    val route: String? = null,
    val isExpandable: Boolean,
    val children: List<DrawerMenuItem> = emptyList(),
    val isExpanded: Boolean = false, // for track state
    val isChild: Boolean = false // for UI indentation
)
