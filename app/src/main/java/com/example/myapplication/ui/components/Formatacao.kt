package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val localBrasil: Locale = Locale.forLanguageTag("pt-BR")

fun Double.emReais(): String = "R$ " + String.format(localBrasil, "%,.2f", this)

fun Double.emReaisSemCentavos(): String = "R$ " + String.format(localBrasil, "%,.0f", this)

fun iconeDaCategoria(categoria: String): ImageVector = when (categoria) {
    "Alimentação" -> Icons.Default.ShoppingCart
    "Transporte" -> Icons.Default.DirectionsBus
    "Moradia" -> Icons.Default.Home
    "Contas" -> Icons.Default.Wifi
    "Lazer" -> Icons.Default.SportsEsports
    "Saúde" -> Icons.Default.LocalHospital
    "Educação" -> Icons.Default.School
    "Salário" -> Icons.Default.Payments
    "Renda extra" -> Icons.Default.WorkOutline
    "Investimentos" -> Icons.AutoMirrored.Filled.TrendingUp
    else -> Icons.Default.Category
}

fun dataDeHoje(): String = SimpleDateFormat("dd/MM/yyyy", localBrasil).format(Date())

fun rotuloData(data: String): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", localBrasil)
    val ontem = Calendar.getInstance()
    ontem.add(Calendar.DAY_OF_YEAR, -1)

    return when (data) {
        formato.format(Date()) -> "Hoje"
        formato.format(ontem.time) -> "Ontem"
        else -> data
    }
}
