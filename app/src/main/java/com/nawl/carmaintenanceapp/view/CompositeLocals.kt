package com.nawl.carmaintenanceapp.view

import androidx.compose.runtime.compositionLocalOf

/**
 * A CompositionLocal to provide the current vehicle's kilometrage down
 * the UI tree without needing to pass it as a parameter explicitly.
 *
 * It defaults to 0 if no value is provided.
 */
val LocalCurrentKilometrage = compositionLocalOf { 0 }