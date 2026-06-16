package com.nevaya.careflow

import android.app.Application
import com.nevaya.careflow.data.repository.RoomRepository

class CareFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RoomRepository.initialize(this)
    }
}
