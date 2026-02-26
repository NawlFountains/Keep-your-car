package com.nawl.carmaintenanceapp.formstate

import java.sql.Date

data class TripLogFormState(
    val origin : String = "",
    val destination : String = "",
    val date : Date = Date(System.currentTimeMillis()),
    val distance : Int = 0,

    val originError : String? = null,
    val destinationError : String? = null,
    val dateError : String? = null,
    val distanceError : String? = null,
)