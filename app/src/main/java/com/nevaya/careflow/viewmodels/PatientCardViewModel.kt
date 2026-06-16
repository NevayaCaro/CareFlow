package com.nevaya.careflow.viewmodels

import androidx.lifecycle.ViewModel
import com.nevaya.careflow.data.models.RoomState
import com.nevaya.careflow.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PatientCardViewModel : ViewModel() {

    private val repository = RoomRepository

    private val _roomState = MutableStateFlow<RoomState?>(null)
    val roomState: StateFlow<RoomState?> = _roomState

    fun loadRoom(roomNumber: Int) {
        _roomState.value = repository.getRoom(roomNumber)
    }

    fun clearRoom(roomNumber: Int) {
        repository.clearRoom(roomNumber)
        loadRoom(roomNumber)
    }
}
