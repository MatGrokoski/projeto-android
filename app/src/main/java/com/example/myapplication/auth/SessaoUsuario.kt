package com.example.myapplication.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal object SessaoUsuario {
    var usuarioId by mutableStateOf<Long?>(null)
        private set
    var usuarioNome by mutableStateOf("")
        private set

    fun iniciar(id: Long, nome: String) {
        require(id > 0)
        usuarioId = id
        usuarioNome = nome
    }

    fun encerrar() {
        usuarioId = null
        usuarioNome = ""
    }

    fun exigirUsuario(): Long = requireNotNull(usuarioId) { "Faça login para continuar" }
}
