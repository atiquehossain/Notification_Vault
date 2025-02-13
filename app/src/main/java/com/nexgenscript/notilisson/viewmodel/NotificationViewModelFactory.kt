package com.nexgenscript.notilisson.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nexgenscript.notilisson.data.NotificationDao

class NotificationViewModelFactory(private val dao: NotificationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
