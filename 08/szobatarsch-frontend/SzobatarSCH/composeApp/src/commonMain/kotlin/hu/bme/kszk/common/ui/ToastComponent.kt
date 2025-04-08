package hu.bme.kszk.common.ui

import androidx.compose.runtime.Composable


// Platformfüggő Toast megvalósítások
@Composable
expect fun ShowToast(message: String)