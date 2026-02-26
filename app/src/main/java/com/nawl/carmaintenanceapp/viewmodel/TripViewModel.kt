package com.nawl.carmaintenanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nawl.carmaintenanceapp.model.dao.TripLogDao

class TripViewModel ( private val tripLogDao: TripLogDao) : ViewModel() {


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