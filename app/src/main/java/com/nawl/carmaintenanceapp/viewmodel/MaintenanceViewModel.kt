package com.nawl.carmaintenanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nawl.carmaintenanceapp.MaintenanceLogFormState
import com.nawl.carmaintenanceapp.model.dao.MaintenanceLogDao
import com.nawl.carmaintenanceapp.model.entities.MaintenanceLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date

class MaintenanceViewModel(private val maintenanceLogDao: MaintenanceLogDao) : ViewModel() {
    private val _formState = MutableStateFlow(MaintenanceLogFormState())
    val formState: StateFlow<MaintenanceLogFormState> = _formState


    fun getLatestMaintenanceLogs(amount: Int): Flow<List<MaintenanceLog>> {
        return maintenanceLogDao.getLatestMaintenanceLogs(amount)
    }

    fun addMaintenanceLog(itemChanged: String, date: Date, mileage: Int, unit: String, notes: String) {
        val newLog = MaintenanceLog(
            itemChanged = itemChanged,
            date = date,
            mileage = mileage,
            unit = unit,
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

    fun onMileageChanged(mileage: Int){
        _formState.value = _formState.value.copy(mileage = mileage, mileageError = null)
    }

    fun onUnitChanged(unit: String){
        _formState.value = _formState.value.copy(unit = unit, unitError = null)
    }

    fun resetFormState() {
        _formState.value = MaintenanceLogFormState()
    }



    fun validate(): Boolean {
        val current = _formState.value

        var itemChangedError: String? = null
        var dateError: String? = null
        var mileageError: String? = null
        var unitError: String? = null

        if (current.itemChanged.isBlank()) {
            itemChangedError = "Item changed is required"
        }

        if (current.date.toString().isBlank()) {
            dateError = "Date is required"
        } else if (isDateInFuture(current.date)) {
            dateError = "Date cannot be in the future"
        }

        if (current.mileage.toString().isBlank()) {
            mileageError = "Mileage is required"
        } else if (current.mileage < 0) {
            mileageError = "Mileage cannot be negative"
        }

        if (current.unit.isBlank()) {
            unitError = "Unit is required"
        } else if (!listOf("km", "miles").contains(current.unit)) {
            unitError = "Unit must be either 'km' or 'miles'"
        }


        _formState.value = current.copy(
            itemChangedError = itemChangedError,
            dateError = dateError,
            mileageError = mileageError,
            unitError = unitError
        )

        return itemChangedError == null && dateError == null && mileageError == null && unitError == null
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