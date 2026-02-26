package com.nawl.carmaintenanceapp.formstate

import java.sql.Date

data class FuelLogFormState(
    val stationName: String = "",
    val quantity: Float = 0f,
    val isTankFull: Boolean = false,
    val date: Date = Date(System.currentTimeMillis()),
    val kilometrage: Int = 0,

    val stationNameError: String? = null,
    val quantityError: String? = null,
    val dateError: String? = null,
    val kilometrageError: String? = null,
)