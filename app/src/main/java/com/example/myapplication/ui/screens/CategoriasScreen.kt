package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.ui.components.CardIndicador
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.ChipFiltro
import com.example.myapplication.ui.components.IconeCategoria
import com.example.myapplication.ui.components.RotuloSecao
import com.example.myapplication.ui.components.emReais
import com.example.myapplication.ui.components.emReaisSemCentavos
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.corDespesa
import com.example.myapplication.ui.theme.corReceita

data class ResumoCategoria(
    val categoria: String,
    val total: Double
)

@Composable
fun CategoriasScreen() {
    var tipoSelecionado by remember { mutableStateOf(TransacaoManager.DESPESA) }

    val transacoesDoTipo = TransacaoManager.listar().filter { it.tipo == tipoSelecionado }

    val totaisPorCategoria = transacoesDoTipo
        .groupBy { it.categoria }
        .map { (categoria, itens) ->
            var soma = 0.0
            for (item in itens) {
                soma += item.valor
            }
            ResumoCategoria(categoria, soma)
        }
        .sortedByDescending { it.total }

    var maiorCategoria = totaisPorCategoria.firstOrNull()
    var menorCategoria = totaisPorCategoria.firstOrNull()
    var somaTotal = 0.0

    for (item in totaisPorCategoria) {
        if (maiorCategoria == null || item.total > maiorCategoria.total) maiorCategoria = item
        if (menorCategoria == null || item.total < menorCategoria.total) menorCategoria = item
        somaTotal += item.total
    }

    val media = if (totaisPorCategoria.isNotEmpty()) somaTotal / totaisPorCategoria.size else 0.0
    val maiorValor = maiorCategoria?.total ?: 1.0
    val corDoTipo = if (tipoSelecionado == TransacaoManager.DESPESA) corDespesa() else corReceita()
    val nomeDoTipo = if (tipoSelecionado == TransacaoManager.DESPESA) "despesas" else "receitas"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            ChipFiltro(
                texto = "Despesas",
                selecionado = tipoSelecionado == TransacaoManager.DESPESA,
                onClick = { tipoSelecionado = TransacaoManager.DESPESA }
            )
            ChipFiltro(
                texto = "Receitas",
                selecionado = tipoSelecionado == TransacaoManager.RECEITA,
                onClick = { tipoSelecionado = TransacaoManager.RECEITA }
            )
        }

        Column {
            RotuloSecao(texto = "Total em $nomeDoTipo")
            Spacer(Modifier.height(6.dp))
            Text(
                text = somaTotal.emReais(),
                style = MaterialTheme.typography.headlineMedium,
                color = corDoTipo
            )
        }

        if (totaisPorCategoria.isEmpty()) {
            CartaoPadrao {
                Text(
                    text = "Ainda não há $nomeDoTipo registradas. Use o + para adicionar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CardIndicador(
                    titulo = "Maior",
                    valor = (maiorCategoria?.total ?: 0.0).emReaisSemCentavos(),
                    detalhe = maiorCategoria?.categoria ?: "-",
                    modifier = Modifier.weight(1f)
                )
                CardIndicador(
                    titulo = "Menor",
                    valor = (menorCategoria?.total ?: 0.0).emReaisSemCentavos(),
                    detalhe = menorCategoria?.categoria ?: "-",
                    modifier = Modifier.weight(1f)
                )
                CardIndicador(
                    titulo = "Média",
                    valor = media.emReaisSemCentavos(),
                    detalhe = "por categoria",
                    modifier = Modifier.weight(1f)
                )
            }

            CartaoPadrao {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    RotuloSecao(
                        texto = "Distribuição",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    totaisPorCategoria.forEach { item ->
                        val percentual = if (somaTotal > 0) (item.total / somaTotal * 100) else 0.0
                        val proporcao = (item.total / maiorValor).toFloat()

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            IconeCategoria(categoria = item.categoria, tipo = tipoSelecionado, tamanho = 40)

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.categoria,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = item.total.emReais(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(corDoTipo.copy(alpha = 0.15f))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(proporcao.coerceAtLeast(0.04f))
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(corDoTipo)
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = "${percentual.toInt()}%",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriasScreenPreview() {
    MyApplicationTheme {
        CategoriasScreen()
    }
}
