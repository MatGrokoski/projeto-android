package com.example.myapplication.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Cadastro

@Serializable
data object Dashboard

@Serializable
data object Transacoes

@Serializable
data object Categorias

@Serializable
data object NovaTransacao

@Serializable
data class DetalheTransacao(val id: Long)

@Serializable
data class EditarTransacao(val id: Long)

@Serializable
data object Perfil
