package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.data.dal.TransacaoDAO
import com.example.myapplication.model.Transacao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransacaoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TransacaoDAO(application)

    var transacoes by mutableStateOf<List<Transacao>>(emptyList())
        private set
    var transacao by mutableStateOf<Transacao?>(null)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var excluida by mutableStateOf(false)
        private set

    val totalReceitas: Double get() {
        var total = 0.0
        for (t in transacoes) { if (t.tipo == TransacaoManager.RECEITA) total += t.valor }
        return total
    }
    val totalDespesas: Double get() {
        var total = 0.0
        for (t in transacoes) { if (t.tipo == TransacaoManager.DESPESA) total += t.valor }
        return total
    }
    val saldo: Double get() = totalReceitas - totalDespesas

    private fun executar(operacao: suspend () -> Unit) {
        if (carregando || salvo) return
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

    fun listar() = executar {
        val uid = SessaoUsuario.exigirUsuario()
        val lista = withContext(Dispatchers.IO) { dao.listar(uid) }
        transacoes = lista
    }

    fun buscarPorId(id: Long) = executar {
        val uid = SessaoUsuario.exigirUsuario()
        val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id, uid) }
        transacao = requireNotNull(encontrado) { "Transação não encontrada." }
    }

    fun adicionar(descricao: String, categoria: String, data: String, valor: Double, tipo: String) = executar {
        require(descricao.trim().length >= 3) { "A descrição deve ter pelo menos 3 caracteres." }
        require(valor > 0) { "Informe um valor maior que zero." }
        val uid = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) {
            dao.inserir(uid, descricao.trim(), categoria, data, valor, tipo)
        }
        salvo = true
    }

    fun alterar(id: Long, descricao: String, categoria: String, data: String, valor: Double, tipo: String) = executar {
        require(descricao.trim().length >= 3) { "A descrição deve ter pelo menos 3 caracteres." }
        require(valor > 0) { "Informe um valor maior que zero." }
        val uid = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) {
            dao.alterar(id, uid, descricao.trim(), categoria, data, valor, tipo)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        val uid = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id, uid) }
        transacoes = transacoes.filterNot { it.id == id }
        excluida = true
    }

    fun consumirSalvo() {
        salvo = false
        transacao = null
    }

    fun consumirExcluida() {
        excluida = false
    }
}
