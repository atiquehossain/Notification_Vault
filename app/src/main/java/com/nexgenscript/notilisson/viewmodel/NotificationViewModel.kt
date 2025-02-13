package com.nexgenscript.notilisson.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nexgenscript.notilisson.data.NotificationDao
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.launch

class NotificationViewModel(private val dao: NotificationDao) : ViewModel() {
    val notifications: LiveData<List<NotificationEntity>> = dao.getFilteredNotifications().asLiveData()


    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            dao.deleteNotification(notification)
        }
    }

    fun getTitlesByApp(appName: String) = dao.getTitlesByApp(appName).asLiveData()
    fun getNotificationsByAppAndTitle(appName: String, title: String) = dao.getNotificationsByAppAndTitle(appName, title).asLiveData()


    fun clearAllNotifications() {
        viewModelScope.launch {
            dao.clearAll()
        }
    }
}
