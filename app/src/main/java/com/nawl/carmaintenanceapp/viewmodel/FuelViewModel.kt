package com.nawl.carmaintenanceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nawl.carmaintenanceapp.formstate.FuelLogFormState
import com.nawl.carmaintenanceapp.model.dao.FuelLogDao
import com.nawl.carmaintenanceapp.model.entities.FuelLog
import com.nawl.carmaintenanceapp.view.ConvertToMetricDistanceUnit
import com.nawl.carmaintenanceapp.view.ConvertToMetricLiquidUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date

class FuelViewModel(private val fuelLogDao: FuelLogDao) : ViewModel() {
    private val _formState = MutableStateFlow(FuelLogFormState())
    val formState: StateFlow<FuelLogFormState> = _formState

    fun getLatestFuelLogs(amount: Int): Flow<List<FuelLog>> {
        return fuelLogDao.getLatestLogs(amount)
    }

    fun addFuelLog(stationName: String, quantity: Float, isTankFull: Boolean, date: Date, kilometrage: Int, notes: String) {
        val newLog = FuelLog(
            stationName = stationName,
            quantity =  ConvertToMetricLiquidUnit(quantity),
            isTankFull = isTankFull,
            date = date,
            kilometrage = ConvertToMetricDistanceUnit(kilometrage),
            notes = notes
        )
        viewModelScope.launch {
            fuelLogDao.insert(newLog)
        }

    }
    fun deleteFuelLog(fuelLog: FuelLog) {
        viewModelScope.launch {
            fuelLogDao.delete(fuelLog)
        }
    }
    fun onStationNameChanged(stationName: String) {
        _formState.value = _formState.value.copy(stationName = stationName, stationNameError = null)
    }
    fun onQuantityChanged(quantity: Float) {
        _formState.value = _formState.value.copy(quantity = quantity, quantityError = null)
    }

    fun onIsTankFullChanged(isTankFull: Boolean) {
        _formState.value = _formState.value.copy(isTankFull = isTankFull)
    }
    fun onDateChanged(date: Date) {
        _formState.value = _formState.value.copy(date = date, dateError = null)
    }
    fun onKilometrageChanged(kilometrage: Int) {
        _formState.value = _formState.value.copy(kilometrage = kilometrage, kilometrageError = null)
    }
    fun resetFormState() {
        _formState.value = FuelLogFormState()
    }
    fun validate(): Boolean {
        val current = _formState.value

        var stationNameError: String? = null
        var quantityError: String? = null
        var dateError: String? = null
        var kilometrageError: String? = null

        if (current.stationName.isBlank()) {
            stationNameError = "Station name is required"
        }

        if (current.quantity.toString().isBlank()) {
            quantityError = "Quantity is required"
        } else if (current.quantity < 0) {
            quantityError = "Quantity cannot be negative"
        }

        if (current.date.toString().isBlank()) {
            dateError = "Date is required"
        } else if (isDateInFuture(current.date)) {
            dateError = "Date cannot be in the future"
        }
        if (current.kilometrage.toString().isBlank()) {
            kilometrageError = "Kilometrage is required"
        } else if (current.kilometrage < 0) {
            kilometrageError = "Kilometrage cannot be negative"
        }
        _formState.value = current.copy(
            stationNameError = stationNameError,
            quantityError = quantityError,
            dateError = dateError,
            kilometrageError = kilometrageError
        )
        return stationNameError == null && quantityError == null && dateError == null && kilometrageError == null
    }

    private fun isDateInFuture(date: Date): Boolean {
        val currentDate = Date(System.currentTimeMillis())
        return date.after(currentDate)
    }
}

class FuelViewModelFactory(private val fuelLogDao: FuelLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FuelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FuelViewModel(fuelLogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}