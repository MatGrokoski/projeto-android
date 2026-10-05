package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.ChipFiltro
import com.example.myapplication.ui.components.LinhaTransacao
import com.example.myapplication.ui.components.rotuloData
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun TransacoesScreen(
    irParaDetalhes: (Int) -> Unit
) {
    val todasTransacoes = TransacaoManager.listar()
    var filtroSelecionado by remember { mutableStateOf("todas") }

    val transacoesFiltradas = when (filtroSelecionado) {
        "receitas" -> todasTransacoes.filter { it.tipo == TransacaoManager.RECEITA }
        "despesas" -> todasTransacoes.filter { it.tipo == TransacaoManager.DESPESA }
        else -> todasTransacoes
    }

    val transacoesAgrupadas = transacoesFiltradas.groupBy { it.data }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            ChipFiltro(
                texto = "Todas",
                selecionado = filtroSelecionado == "todas",
                onClick = { filtroSelecionado = "todas" }
            )
            ChipFiltro(
                texto = "Receitas",
                selecionado = filtroSelecionado == "receitas",
                onClick = { filtroSelecionado = "receitas" }
            )
            ChipFiltro(
                texto = "Despesas",
                selecionado = filtroSelecionado == "despesas",
                onClick = { filtroSelecionado = "despesas" }
            )
        }

        if (transacoesFiltradas.isEmpty()) {
            Text(
                text = "Nenhuma transação encontrada para esse filtro.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(20.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                transacoesAgrupadas.forEach { (data, transacoesDoDia) ->
                    item(key = data) {
                        Text(
                            text = rotuloData(data),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp, bottom = 2.dp, start = 4.dp)
                        )
                    }
                    items(
                        items = transacoesDoDia,
                        key = { it.id }
                    ) { transacao ->
                        CartaoPadrao {
                            LinhaTransacao(
                                transacao = transacao,
                                onClick = { irParaDetalhes(transacao.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransacoesScreenPreview() {
    MyApplicationTheme {
        TransacoesScreen(irParaDetalhes = {})
    }
}
