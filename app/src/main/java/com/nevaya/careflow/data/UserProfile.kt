package com.nevaya.careflow.data

data class UserProfile(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val dob: String,
    val profileImageUri: String?
) {
    companion object {

        fun toListString(list: List<UserProfile>): String =
            list.joinToString(";;") {
                "${it.name}|${it.username}|${it.email}|${it.password}|${it.dob}|${it.profileImageUri}"
            }

        // ⭐ Add this function here
        fun fromListString(raw: String?): List<UserProfile> {
            if (raw.isNullOrBlank()) return emptyList()

            return raw.split(";;").mapNotNull { entry ->
                val parts = entry.split("|")

                when (parts.size) {
                    5 -> UserProfile(
                        name = parts[0],
                        username = parts[1],
                        email = parts[2],
                        password = parts[3],
                        dob = parts[4],
                        profileImageUri = null
                    )
                    6 -> UserProfile(
                        name = parts[0],
                        username = parts[1],
                        email = parts[2],
                        password = parts[3],
                        dob = parts[4],
                        profileImageUri = parts[5].ifBlank { null }
                    )
                    else -> null
                }
            }
        }
    }
}