package com.nevaya.careflow.data.repository

import android.content.Context
import com.nevaya.careflow.data.datastore.RoomDataStore
import com.nevaya.careflow.data.model.Patient
import com.nevaya.careflow.data.models.RoomState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object RoomRepository {

    private lateinit var dataStore: RoomDataStore
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _rooms = MutableStateFlow<List<RoomState>>(emptyList())
    val rooms: StateFlow<List<RoomState>> = _rooms

    fun initialize(context: Context) {
        dataStore = RoomDataStore(context)

        scope.launch {
            dataStore.getRooms().collect { savedRooms ->
                if (savedRooms.isEmpty()) {
                    // Initialize 20 empty rooms
                    val initialRooms = List(60) { index ->
                        RoomState(roomNumber = index + 1)
                    }
                    _rooms.value = initialRooms
                    dataStore.saveRooms(initialRooms)
                } else {
                    _rooms.value = savedRooms
                }
            }
        }
    }

    fun getRoom(roomNumber: Int): RoomState? {
        return _rooms.value.firstOrNull { it.roomNumber == roomNumber }
    }

    fun assignPatient(roomNumber: Int, patient: Patient) {
        val updated = _rooms.value.map {
            if (it.roomNumber == roomNumber) it.copy(patient = patient) else it
        }
        _rooms.value = updated
        save(updated)
    }

    fun clearRoom(roomNumber: Int) {
        val updated = _rooms.value.map {
            if (it.roomNumber == roomNumber) it.copy(patient = null) else it
        }
        _rooms.value = updated
        save(updated)
    }

    private fun save(rooms: List<RoomState>) {
        scope.launch {
            dataStore.saveRooms(rooms)
        }
    }
}
