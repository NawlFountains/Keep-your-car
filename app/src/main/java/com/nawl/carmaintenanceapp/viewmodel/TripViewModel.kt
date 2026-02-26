package com.nawl.carmaintenanceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nawl.carmaintenanceapp.formstate.TripLogFormState
import com.nawl.carmaintenanceapp.model.dao.TripLogDao
import com.nawl.carmaintenanceapp.model.entities.TripLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date

class TripViewModel ( private val tripLogDao: TripLogDao) : ViewModel() {
    private val _formState = MutableStateFlow(TripLogFormState())
    val formState: StateFlow<TripLogFormState> = _formState

    fun getLatestTripLogs(amount: Int): Flow<List<TripLog>> {
        return tripLogDao.getLatestLogs(amount)
    }

    fun addTripLog(origin: String, destination: String, date: Date, distance: Int) {
        val newLog = TripLog(
            origin = origin,
            destination = destination,
            date = date,
            distance = distance
        )
        viewModelScope.launch {
            tripLogDao.insert(newLog)
        }
    }

    fun editTripLog(id: Int, origin: String, destination: String, date: Date, distance: Int) {

        val tripLog = TripLog(
            id = id,
            origin = origin,
            destination = destination,
            date = date,
            distance = distance
        )
        viewModelScope.launch {
            tripLogDao.update(tripLog)
        }

    }

    fun deleteTripLog(tripLog: TripLog) {
        viewModelScope.launch {
            tripLogDao.delete(tripLog)
        }
    }

    fun resetFormState() {
        _formState.value = TripLogFormState()
    }

    fun onOriginChanged(origin: String) {
        _formState.value = _formState.value.copy(origin = origin, originError = null)
    }

    fun onDestinationChanged(destination: String) {
        _formState.value = _formState.value.copy(destination = destination, destinationError = null)
    }

    fun onDistanceChanged(distance: Int) {
        _formState.value = _formState.value.copy(distance = distance, distanceError = null)
    }

    fun onDateChanged(date: Date) {
        _formState.value = _formState.value.copy(date = date, dateError = null)
    }

    fun validate(): Boolean {
        val current = _formState.value

        var originError = if (current.origin.isBlank()) "Origin is required" else null
        var destinationError = if (current.destination.isBlank()) "Destination is required" else null
        var dateError : String? = null
        var distanceError : String? = null

        if (current.date.toString().isBlank()) {
            dateError = "Date is required"
        } else if (isDateInFuture(current.date)) {
            dateError = "Date cannot be in the future"
        }
        if (current.distance.toString().isBlank()) {
            distanceError = "Distance is required"
        } else if (current.distance < 0) {
            distanceError = "Distance cannot be negative"
        } else if (current.distance == 0) {
            distanceError = "Distance cannot be 0"
        }
        _formState.value = current.copy(
            originError = originError,
            destinationError = destinationError,
            dateError = dateError,
            distanceError = distanceError
        )
        return originError == null && destinationError == null && dateError == null && distanceError == null
    }

    private fun isDateInFuture(date: Date): Boolean {
        val currentDate = Date(System.currentTimeMillis())
        return date.after(currentDate)
    }
}

class TripViewModelFactory(private val tripLogDao: TripLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(tripLogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}