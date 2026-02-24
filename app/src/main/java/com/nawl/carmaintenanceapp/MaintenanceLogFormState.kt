package com.nawl.carmaintenanceapp

import java.sql.Date

data class MaintenanceLogFormState(
    val itemChanged: String = "",
    val date: Date = Date(System.currentTimeMillis()),
    val mileage: Int = 0,
    val unit: String = "km",

    val itemChangedError : String? = null,
    val dateError : String? = null,
    val mileageError : String? = null,
    val unitError : String? = null,
)