package com.nawl.carmaintenanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nawl.carmaintenanceapp.model.dao.FuelLogDao

class FuelViewModel(private val fuelLogDao: FuelLogDao) : ViewModel() {

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