package com.nevaya.careflow.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserProfile(
    val name: String = "John Doe",
    val username: String = "@johndoe",
    val email: String = "john.doe@example.com",
    val phone: String = "555-123-4567"
)

object UserRepository {

    var profile by mutableStateOf(UserProfile())
        private set

    var twoFactorEnabled by mutableStateOf(false)
        private set

    var notificationsEnabled by mutableStateOf(true)
        private set

    fun updateProfile(
        name: String,
        username: String,
        email: String,
        phone: String
    ) {
        profile = profile.copy(
            name = name,
            username = username,
            email = email,
            phone = phone
        )
    }

    fun changePassword(current: String, new: String): Boolean {
        return current.isNotBlank() && new.isNotBlank() && current != new
    }

    // ⭐ Correct names — no JVM clash
    fun updateTwoFactor(enabled: Boolean) {
        twoFactorEnabled = enabled
    }

    fun updateNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
    }

    fun clearCache() {
        // Placeholder
    }

    fun requestDataExport() {
        // Placeholder
    }

    fun deleteAccount(): Boolean {
        return true
    }

    fun setTwoFactorEnabled(): Boolean {
        return true
    }
}