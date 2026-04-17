package com.nevaya.careflow.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue



object UserRepository {

    var twoFactorEnabled by mutableStateOf(false)
        private set

    var notificationsEnabled by mutableStateOf(true)
        private set


    fun changePassword(current: String, new: String): Boolean {
        return current.isNotBlank() && new.isNotBlank() && current != new
    }

    fun updateTwoFactor(enabled: Boolean) {
        twoFactorEnabled = enabled
    }

    fun updateNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
    }

    fun clearCache() {}

    fun requestDataExport() {}

    fun deleteAccount(): Boolean = true
}