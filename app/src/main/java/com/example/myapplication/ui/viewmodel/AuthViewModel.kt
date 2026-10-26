package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.dal.UsuarioDAO
import com.example.myapplication.validation.CadastroValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDAO(application)

    var carregando by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var concluido by mutableStateOf(false)
        private set

    private fun executar(op: suspend () -> Boolean) {
        if (carregando || concluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                concluido = withContext(Dispatchers.IO) { op() }
                if (!concluido) mensagem = "E-mail ou senha inválido."
            } catch (e: SQLiteConstraintException) {
                mensagem = "Não foi possível cadastrar. Confira se o e-mail já está em uso."
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun cadastrar(nome: String, email: String, senha: String) = executar {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase(Locale.ROOT)
        val senhaLimpa = senha.trim()

        CadastroValidator.validar(nomeLimpo, emailLimpo, senhaLimpa)
        val salt = AuthManager.criarSalt()
        val senhaHash = AuthManager.gerarHash(senhaLimpa, salt)
        try {
            dao.cadastrar(nomeLimpo, emailLimpo, senhaHash, salt)
            true
        } finally {
            senhaHash.fill(0)
        }
    }

    fun login(email: String, senha: String) = executar {
        require(email.length <= 254 && senha.length in 6..128) {
            "E-mail ou senha inválidos."
        }
        val dados = withContext(Dispatchers.IO) {
            dao.buscarSenha(email.trim().lowercase(Locale.ROOT))
        }
        val senhaHash = dados?.hash ?: ByteArray(32)
        val salt = dados?.salt ?: ByteArray(16)
        try {
            val autenticado = AuthManager.validarSenha(senha, salt, senhaHash)
            if (autenticado && dados != null) {
                SessaoUsuario.iniciar(dados.usuarioId, dados.nome)
                true
            } else false
        } finally {
            senhaHash.fill(0)
        }
    }

    fun consumirResultado() {
        concluido = false
    }
}
