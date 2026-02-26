package com.nawl.carmaintenanceapp.formstate

import java.sql.Date

data class MaintenanceLogFormState(
    val itemChanged: String = "",
    val date: Date = Date(System.currentTimeMillis()),
    val kilometrage: Int = 0,

    val itemChangedError : String? = null,
    val dateError : String? = null,
    val kilometrageError : String? = null,
)