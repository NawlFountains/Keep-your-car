package com.nawl.carmaintenanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nawl.carmaintenanceapp.formstate.MaintenanceLogFormState
import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogDao
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import com.nawl.carmaintenanceapp.view.ConvertToMetricDistanceUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date

class MaintenanceViewModel(private val maintenanceLogDao: MaintenanceLogDao) : ViewModel() {
    private val _formState = MutableStateFlow(MaintenanceLogFormState())
    val formState: StateFlow<MaintenanceLogFormState> = _formState


    fun getLatestMaintenanceLogs(amount: Int): Flow<List<MaintenanceLog>> {
        return maintenanceLogDao.getLatestLogs(amount)
    }

    fun addMaintenanceLog(itemChanged: String, date: Date, kilometrage: Int, notes: String) {
        val newLog = MaintenanceLog(
            itemChanged = itemChanged,
            date = date,
            kilometrage = ConvertToMetricDistanceUnit(kilometrage),
            notes = notes
        )
        viewModelScope.launch {
            maintenanceLogDao.insert(newLog)
        }
    }
    fun deleteMaintenanceLog(maintenanceLog: MaintenanceLog) {
        viewModelScope.launch {
            maintenanceLogDao.delete(maintenanceLog)
        }
    }


    fun onItemChanged(itemChanged: String) {
        _formState.value = _formState.value.copy(itemChanged = itemChanged, itemChangedError = null)
    }

    fun onDateChanged(date: Date){
        _formState.value = _formState.value.copy(date = date, dateError = null)
    }

    fun onKilometrageChanged(kilometrage: Int){
        _formState.value = _formState.value.copy(kilometrage = kilometrage, kilometrageError = null)
    }

    fun resetFormState() {
        _formState.value = MaintenanceLogFormState()
    }



    fun validate(): Boolean {
        val current = _formState.value

        var itemChangedError: String? = null
        var dateError: String? = null
        var kilometrageError: String? = null

        if (current.itemChanged.isBlank()) {
            itemChangedError = "Item changed is required"
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
            itemChangedError = itemChangedError,
            dateError = dateError,
            kilometrageError = kilometrageError
        )

        return itemChangedError == null && dateError == null && kilometrageError == null
    }
    private fun isDateInFuture(date: Date): Boolean {
        val currentDate = Date(System.currentTimeMillis())
        return date.after(currentDate)
    }
}

class MaintenanceViewModelFactory(private val maintenanceLogDao: MaintenanceLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaintenanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MaintenanceViewModel(maintenanceLogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}