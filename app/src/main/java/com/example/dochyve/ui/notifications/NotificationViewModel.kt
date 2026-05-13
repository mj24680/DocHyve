package com.example.dochyve.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dochyve.data.model.Notification
import com.example.dochyve.data.repository.NotificationRepository

class NotificationViewModel : ViewModel() {
    private val repository = NotificationRepository()

    private val _notifications = MutableLiveData<List<Notification>>()
    val notification : LiveData<List<Notification>> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        _notifications.value = repository.getNotifications()
    }
}