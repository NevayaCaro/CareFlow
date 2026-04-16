package com.nevaya.careflow.data

import com.nevaya.careflow.data.NurseAssignment

// This is in-memory "database"
object SessionStore {

    private val sessions = mutableMapOf<String, Session>()

    fun createSession(code: String): Session {
        val session = Session(
            code = code,
            workerCode = "",
            creatorCode = "",
            rooms = emptyList(),
            assignments = emptyList()
        )
        sessions[code] = session
        return session
    }

    fun getSession(code: String): Session? {
        return sessions[code]
    }
}