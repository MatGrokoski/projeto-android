package com.example.myapplication.data

object TransacaoManager {
    const val RECEITA = "receita"
    const val DESPESA = "despesa"

    val categoriasDespesa = listOf(
        "Alimentação", "Transporte", "Moradia", "Contas",
        "Lazer", "Saúde", "Educação", "Outros"
    )
    val categoriasReceita = listOf("Salário", "Renda extra", "Investimentos", "Outros")
}
