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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.LinhaTransacao
import com.example.myapplication.ui.components.TituloSecao
import com.example.myapplication.ui.components.emReais
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun DashboardScreen(
    irParaTransacoes: () -> Unit,
    irParaDetalhes: (Int) -> Unit
) {
    val ultimasTransacoes = TransacaoManager.listar().take(4)
    val receitas = TransacaoManager.totalReceitas()
    val despesas = TransacaoManager.totalDespesas()
    val saldo = TransacaoManager.saldo()

    val nome = AuthManager.usuarioLogado?.nome?.split(" ")?.firstOrNull() ?: ""
    val saudacao = if (nome.isNotEmpty()) "Olá, $nome" else "Olá"

    val proporcaoGasta = if (receitas > 0) (despesas / receitas).toFloat().coerceIn(0f, 1f) else 0f
    val percentualGasto = if (receitas > 0) (despesas / receitas * 100).toInt() else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = saudacao,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Veja como está o seu dinheiro este mês.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Saldo disponível",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = saldo.emReais(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(proporcaoGasta)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Você já gastou $percentualGasto% do que entrou.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResumoNoSaldo(
                        icone = Icons.Default.ArrowDownward,
                        titulo = "Entradas",
                        valor = receitas.emReais(),
                        modifier = Modifier.weight(1f)
                    )
                    ResumoNoSaldo(
                        icone = Icons.Default.ArrowUpward,
                        titulo = "Saídas",
                        valor = despesas.emReais(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        TituloSecao(
            texto = "Últimas transações",
            textoAcao = "Ver todas",
            onAcao = irParaTransacoes
        )

        Spacer(Modifier.height(4.dp))

        CartaoPadrao {
            if (ultimasTransacoes.isEmpty()) {
                Text(
                    text = "Nenhuma transação ainda. Toque no + para registrar a primeira.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(20.dp)
                )
            } else {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ultimasTransacoes.forEachIndexed { indice, transacao ->
                        LinhaTransacao(
                            transacao = transacao,
                            onClick = { irParaDetalhes(transacao.id) }
                        )
                        if (indice < ultimasTransacoes.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.padding(start = 74.dp, end = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumoNoSaldo(
    icone: ImageVector,
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f))
            .padding(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f))
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MyApplicationTheme {
        DashboardScreen(irParaTransacoes = {}, irParaDetalhes = {})
    }
}
