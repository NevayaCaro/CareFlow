package com.nevaya.careflow.data

object SessionStore {

    private val sessions = mutableMapOf<String, Session>()

    // optional: remembers last opened creator session
    var lastCreatorSessionCode: String? = null


    fun createSession(code: String): Session {
        val session = Session(
            code = code,
            workerCode = code,
            creatorCode = code, // same code system (simplified + stable)
            rooms = emptyList(),
            assignments = emptyList()
        )

        sessions[code] = session
        return session
    }


    fun getSession(code: String?): Session? {
        if (code == null) return null
        return sessions[code]
    }


    fun getCreatorSession(code: String?): Session? {
        val finalCode = code ?: lastCreatorSessionCode
        return sessions[finalCode]
    }


    fun getWorkerSession(code: String): Session? {
        return sessions[code]
    }
    fun getSessionByCreatorCode(code: String): Session? {
        return sessions.values.find { it.creatorCode == code }
    }
}
