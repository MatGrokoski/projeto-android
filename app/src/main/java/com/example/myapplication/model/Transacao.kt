package com.example.myapplication.model

data class Transacao(
    val id: Int,
    val descricao: String,
    val categoria: String,
    val data: String,
    val valor: Double,
    val tipo: String
)
