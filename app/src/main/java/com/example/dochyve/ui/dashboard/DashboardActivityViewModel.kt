package com.example.dochyve.ui.dashboard

import androidx.lifecycle.*
import com.example.dochyve.data.model.DrawerMenuItem
import com.example.dochyve.data.repository.DashboardRepository
import kotlinx.coroutines.launch

class DashboardActivityViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _menuList = MutableLiveData<List<DrawerMenuItem>>()
    val menuList: LiveData<List<DrawerMenuItem>> = _menuList

    private val _currentTitle = MutableLiveData<String>()
    val currentTitle: LiveData<String> = _currentTitle

    private var originalList = listOf<DrawerMenuItem>()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        viewModelScope.launch {
            try {
                originalList = repository.getMenuItems()
                _menuList.value = buildDisplayList(originalList)
            } catch (e: Exception) {
                _menuList.value = emptyList()
            }
        }
    }

    // for handling expand & collapse
    fun toggleItem(clickedItem: DrawerMenuItem){
        originalList = originalList.map {
            if (it.id == clickedItem.id){
                it.copy(isExpanded = !it.isExpanded)
            }else it
        }
        _menuList.value = buildDisplayList(originalList)
    }

    // flattened nested list for recycler view
    private fun buildDisplayList(list: List<DrawerMenuItem>): List<DrawerMenuItem>{
        val result = mutableListOf<DrawerMenuItem>()
        list.forEach { item ->
            result.add(item)
            if(item.isExpanded){
                result.addAll(item.children)
            }
        }
        return result
    }

    fun updateTitle(title: String) {
        _currentTitle.value = title
    }
}