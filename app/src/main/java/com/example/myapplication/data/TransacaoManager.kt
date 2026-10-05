package com.example.myapplication.data

import com.example.myapplication.model.Transacao

object TransacaoManager {

    const val RECEITA = "receita"
    const val DESPESA = "despesa"

    val categoriasDespesa = listOf("Alimentação", "Transporte", "Moradia", "Contas", "Lazer", "Saúde", "Educação", "Outros")
    val categoriasReceita = listOf("Salário", "Renda extra", "Investimentos", "Outros")

    private var proximoId = 7

    private val transacoes = mutableListOf(
        Transacao(1, "Mercado", "Alimentação", "05/10/2026", 180.0, DESPESA),
        Transacao(2, "Uber para a faculdade", "Transporte", "05/10/2026", 32.0, DESPESA),
        Transacao(3, "Aluguel", "Moradia", "04/10/2026", 950.0, DESPESA),
        Transacao(4, "Internet", "Contas", "04/10/2026", 89.90, DESPESA),
        Transacao(5, "Salário", "Salário", "01/10/2026", 3500.0, RECEITA),
        Transacao(6, "Freelance de design", "Renda extra", "01/10/2026", 600.0, RECEITA)
    )

    fun listar(): List<Transacao> = transacoes.toList()

    fun buscarPorId(id: Int): Transacao? = transacoes.find { it.id == id }

    fun adicionar(descricao: String, categoria: String, data: String, valor: Double, tipo: String) {
        transacoes.add(0, Transacao(proximoId, descricao.trim(), categoria, data, valor, tipo))
        proximoId++
    }

    fun remover(id: Int): Boolean = transacoes.removeIf { it.id == id }

    fun totalReceitas(): Double = transacoes.filter { it.tipo == RECEITA }.sumOf { it.valor }

    fun totalDespesas(): Double = transacoes.filter { it.tipo == DESPESA }.sumOf { it.valor }

    fun saldo(): Double = totalReceitas() - totalDespesas()
}
