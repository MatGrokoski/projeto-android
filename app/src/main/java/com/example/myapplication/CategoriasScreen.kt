package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

// Representa o total gasto em uma categoria — calculado a partir da lista de transações
data class ResumoCategoria(
    val categoria: String,
    val total: Double
)

@Composable
fun CategoriasScreen(
    modifier: Modifier = Modifier,
    aoClicarInicio: () -> Unit = {},
    aoClicarTransacoes: () -> Unit = {},
    aoClicarAdicionar: () -> Unit = {}
) {

    val transacoes = remember { gerarTodasTransacoes() }

    // Considera apenas despesas para o resumo por categoria
    val despesas = transacoes.filter { it.tipo == "despesa" }

    // Agrupa por categoria e soma os valores de cada uma (mesma lógica de totalNotas dos exercícios)
    val totaisPorCategoria = despesas
        .groupBy { it.categoria }
        .map { (categoria, itens) ->
            var soma = 0.0
            for (item in itens) {
                soma += item.valor
            }
            ResumoCategoria(categoria, soma)
        }
        .sortedByDescending { it.total }

    // Encontra maior, menor e média "na mão", com for/if — mesmo padrão dos exercícios de notas
    var maiorCategoria = totaisPorCategoria.firstOrNull()
    var menorCategoria = totaisPorCategoria.firstOrNull()
    var somaTotal = 0.0

    for (item in totaisPorCategoria) {
        if (maiorCategoria == null || item.total > maiorCategoria.total) maiorCategoria = item
        if (menorCategoria == null || item.total < menorCategoria.total) menorCategoria = item
        somaTotal += item.total
    }

    val media = if (totaisPorCategoria.isNotEmpty()) somaTotal / totaisPorCategoria.size else 0.0
    val maiorValor = maiorCategoria?.total ?: 1.0 // usado para calcular a proporção das barras

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------- Cabeçalho ----------
        Text(
            text = "Resumo por categoria",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ---------- Seletor de mês (estático por enquanto) ----------
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Mês anterior")
                Text(text = "Agosto 2026", fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Próximo mês")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---------- Cards Maior / Menor / Média ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CardIndicador(
                titulo = "MAIOR GASTO",
                linha1 = maiorCategoria?.categoria ?: "-",
                linha2 = "R$ ${maiorCategoria?.total?.toInt() ?: 0}",
                modifier = Modifier.weight(1f)
            )
            CardIndicador(
                titulo = "MENOR GASTO",
                linha1 = menorCategoria?.categoria ?: "-",
                linha2 = "R$ ${menorCategoria?.total?.toInt() ?: 0}",
                modifier = Modifier.weight(1f)
            )
            CardIndicador(
                titulo = "MÉDIA MENSAL",
                linha1 = "",
                linha2 = "R$ ${media.toInt()}",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Gráfico de barras ----------
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "GRÁFICO DE GASTOS",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(14.dp))

                totaisPorCategoria.forEach { item ->
                    val proporcao = (item.total / maiorValor).toFloat()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = item.categoria,
                            modifier = Modifier.width(90.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(14.dp)
                                .background(
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(proporcao.coerceAtLeast(0.05f))
                                    .height(14.dp)
                                    .background(
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.4f + proporcao * 0.6f),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "R$ ${item.total.toInt()}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Distribuição percentual ----------
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "DISTRIBUIÇÃO",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(10.dp))

                totaisPorCategoria.forEach { item ->
                    val percentual = if (somaTotal > 0) (item.total / somaTotal * 100) else 0.0

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = iconeParaTransacao(item.categoria),
                                contentDescription = item.categoria,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.categoria, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${percentual.toInt()}% do total",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = "R$ ${"%.2f".format(item.total).replace(".", ",")}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- Navegação inferior ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemNavegacao(icone = Icons.Default.Home, texto = "Início", selecionado = false, onClick = aoClicarInicio)
            ItemNavegacao(icone = Icons.Default.List, texto = "Transações", selecionado = false, onClick = aoClicarTransacoes)

            Surface(
                color = MaterialTheme.colorScheme.error,
                shape = CircleShape,
                modifier = Modifier.clickable { aoClicarAdicionar() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar transação",
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            ItemNavegacao(icone = Icons.Default.GridView, texto = "Categorias", selecionado = true)
        }
    }
}

@Composable
fun CardIndicador(titulo: String, linha1: String, linha2: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            if (linha1.isNotEmpty()) {
                Text(text = linha1, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Text(text = linha2, fontWeight = FontWeight.Bold)
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
