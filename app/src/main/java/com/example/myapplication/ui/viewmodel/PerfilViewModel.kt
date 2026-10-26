package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.Usuario
import com.example.myapplication.data.dal.UsuarioDAO
import com.example.myapplication.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDAO(application)

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var contaExcluida by mutableStateOf(false)
        private set

    private fun executar(operacao: suspend () -> Unit) {
        if (carregando || salvo || contaExcluida) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                operacao()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun carregar() {
        val id = SessaoUsuario.usuarioId ?: return
        if (usuario?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            usuario = requireNotNull(encontrado) { "Usuário não encontrado." }
            nome = encontrado.nome
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun salvar(novaSenha: String) = executar {
        val selecionado = requireNotNull(usuario) { "Carregue o perfil primeiro." }
        CadastroValidator.validar(nome.trim(), novaSenha)
        withContext(Dispatchers.IO) {
            if (novaSenha.isEmpty()) {
                dao.alterar(selecionado.id, nome.trim())
            } else {
                val salt = AuthManager.criarSalt()
                val hash = AuthManager.gerarHash(novaSenha.trim(), salt)
                try {
                    dao.alterar(selecionado.id, nome.trim(), hash, salt)
                } finally {
                    hash.fill(0)
                }
            }
        }
        SessaoUsuario.iniciar(selecionado.id, nome.trim())
        salvo = true
    }

    fun excluirConta() = executar {
        val id = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id) }
        SessaoUsuario.encerrar()
        contaExcluida = true
    }

    fun consumirSalvo() {
        salvo = false
    }
}
