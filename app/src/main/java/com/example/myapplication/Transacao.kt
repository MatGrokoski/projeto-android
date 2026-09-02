package com.example.myapplication

data class Transacao(
    val id: Int,
    val nome: String,
    val categoria: String,
    val data: String,
    val valor: Double,
    val tipo: String // "receita" ou "despesa"
)
