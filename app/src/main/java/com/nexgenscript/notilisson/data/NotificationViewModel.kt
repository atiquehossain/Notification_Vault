package com.nexgenscript.notilisson

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nexgenscript.notilisson.data.NotificationDao
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.launch

class NotificationViewModel(private val dao: NotificationDao) : ViewModel() {
    val notifications: LiveData<List<NotificationEntity>> = dao.getFilteredNotifications().asLiveData()
    val notificationCount: LiveData<Int> = dao.getFilteredNotificationCount().asLiveData() // Observe count


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
