package com.example.myapplication

// Lista de transações de exemplo, usada nas telas de Dashboard, Transações e Categorias.
// Futuramente pode ser substituída por dados reais (banco de dados / API),
// mas por enquanto usamos uma lista fixa, coerente com o conteúdo já visto em aula.
fun gerarTodasTransacoes(): List<Transacao> {
    return listOf(
        Transacao(1, "Mercado", "Alimentação", "Hoje", 180.0, "despesa"),
        Transacao(2, "Uber", "Transporte", "Hoje", 32.0, "despesa"),
        Transacao(3, "Aluguel", "Moradia", "Ontem", 950.0, "despesa"),
        Transacao(4, "Internet", "Contas", "Ontem", 89.90, "despesa"),
        Transacao(5, "Salário", "Renda", "22 de agosto", 3500.0, "receita"),
        Transacao(6, "Freelance", "Renda extra", "22 de agosto", 600.0, "receita")
    )
}
