package com.nevaya.careflow.data

object SessionStore {

    private val sessions = mutableMapOf<String, Session>()

    // Create session
    fun createSession(code: String): Session {
        val session = Session(
            code = code,
            workerCode = code, // worker uses session code
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


    fun getSessionByCreatorCode(code: String): Session? {
        return sessions.values.find { it.creatorCode == code }
    }
}