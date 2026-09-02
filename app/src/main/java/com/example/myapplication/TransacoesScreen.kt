package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun TransacoesScreen(
    modifier: Modifier = Modifier,
    aoClicarInicio: () -> Unit = {},
    aoClicarAdicionar: () -> Unit = {},
    aoClicarCategorias: () -> Unit = {}
) {

    val todasTransacoes = remember { gerarTodasTransacoes() }
    var filtroSelecionado by remember { mutableStateOf("todas") }

    // Filtra a lista de acordo com o chip selecionado (uso de "when" + "filter", já vistos em aula)
    val transacoesFiltradas = when (filtroSelecionado) {
        "receitas" -> todasTransacoes.filter { it.tipo == "receita" }
        "despesas" -> todasTransacoes.filter { it.tipo == "despesa" }
        else -> todasTransacoes
    }

    // Agrupa as transações filtradas por data, mantendo a ordem em que aparecem
    val transacoesAgrupadas = transacoesFiltradas.groupBy { it.data }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------- Cabeçalho ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transações",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filtros avançados"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Chips de filtro ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Lista agrupada por data ----------
        Column(modifier = Modifier.weight(1f)) {
            transacoesAgrupadas.forEach { (data, transacoesDoDia) ->
                Text(
                    text = data.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
                )
                Card(shape = RoundedCornerShape(14.dp)) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        transacoesDoDia.forEach { transacao ->
                            LinhaTransacao(transacao)
                        }
                    }
                }
            }

            if (transacoesFiltradas.isEmpty()) {
                Text(
                    text = "Nenhuma transação encontrada para esse filtro.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }

        // ---------- Navegação inferior ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemNavegacao(icone = Icons.Default.Home, texto = "Início", selecionado = false, onClick = aoClicarInicio)
            ItemNavegacao(icone = Icons.Default.List, texto = "Transações", selecionado = true)

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

            ItemNavegacao(icone = Icons.Default.GridView, texto = "Categorias", selecionado = false, onClick = aoClicarCategorias)
        }
    }
}

@Composable
fun ChipFiltro(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(50),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = texto,
            color = if (selecionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransacoesScreenPreview() {
    MyApplicationTheme {
        TransacoesScreen()
    }
}
