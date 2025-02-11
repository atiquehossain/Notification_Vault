package com.nexgenscript.notilisson.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch




import androidx.lifecycle.asLiveData


class NotificationViewModel(private val dao: NotificationDao) : ViewModel() {
    val notifications: LiveData<List<NotificationEntity>> = dao.getAllNotifications().asLiveData()

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            dao.deleteNotification(notification)
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }
}


