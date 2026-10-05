package com.example.myapplication.auth

object AuthManager {
    data class User(
        val email: String,
        val senha: String,
        val nome: String = ""
    )

    private val usuarios = mutableListOf(
        User("lucas@meubolso.com", "123456", "Lucas")
    )

    var usuarioLogado: User? = null
        private set

    fun login(email: String, senha: String): Boolean {
        val usuario = usuarios.find { it.email == email.trim() && it.senha == senha }
        usuarioLogado = usuario
        return usuario != null
    }

    fun cadastrar(nome: String, email: String, senha: String): Boolean {
        if (usuarios.any { it.email == email.trim() }) return false

        usuarios.add(User(email.trim(), senha, nome.trim()))
        return true
    }

    fun sair() {
        usuarioLogado = null
    }
}
