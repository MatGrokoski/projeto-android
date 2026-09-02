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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

// Cor de sucesso (receita) — o tema padrão não tem "verde", usamos uma cor fixa aqui
val CorReceita = Color(0xFF2E8B57)

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    aoClicarTransacoes: () -> Unit = {},
    aoClicarAdicionar: () -> Unit = {},
    aoClicarCategorias: () -> Unit = {}
) {

    val transacoes = remember { gerarTransacoesRecentes() }

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
            Column {
                Text(
                    text = "Olá, Lucas",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Meu Bolso • Finanças",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- Card de saldo ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "SALDO DO MÊS",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "R$ 1.240,00",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Cards Receitas e Despesas ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RECEITAS",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = CorReceita,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "R$ 3.500,00",
                        fontWeight = FontWeight.Bold,
                        color = CorReceita
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DESPESAS",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "R$ 2.260,00",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- Últimas transações ----------
        Text(
            text = "Últimas transações",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(shape = RoundedCornerShape(14.dp)) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                transacoes.forEach { transacao ->
                    LinhaTransacao(transacao)
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
            ItemNavegacao(icone = Icons.Default.Home, texto = "Início", selecionado = true)
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
                    modifier = Modifier
                        .padding(12.dp)
                        .size(22.dp)
                )
            }

            ItemNavegacao(icone = Icons.Default.GridView, texto = "Categorias", selecionado = false, onClick = aoClicarCategorias)
        }
    }
}

@Composable
fun LinhaTransacao(transacao: Transacao) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            shape = CircleShape
        ) {
            Icon(
                imageVector = iconeParaTransacao(transacao.nome),
                contentDescription = transacao.categoria,
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = transacao.nome, fontWeight = FontWeight.Bold)
            Text(
                text = transacao.data,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        val valorFormatado = if (transacao.tipo == "receita") {
            "+ R$ ${"%.2f".format(transacao.valor).replace(".", ",")}"
        } else {
            "- R$ ${"%.2f".format(transacao.valor).replace(".", ",")}"
        }

        Text(
            text = valorFormatado,
            fontWeight = FontWeight.Bold,
            color = if (transacao.tipo == "receita") CorReceita else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ItemNavegacao(
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    selecionado: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icone,
            contentDescription = texto,
            tint = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

// Escolhe um ícone simples baseado no nome da transação OU na categoria (mesma lógica de "when" vista em aula)
fun iconeParaTransacao(nomeOuCategoria: String) = when (nomeOuCategoria) {
    "Mercado", "Alimentação" -> Icons.Default.ShoppingCart
    "Salário", "Renda" -> Icons.Default.CreditCard
    "Uber", "Transporte" -> Icons.Default.DirectionsCar
    "Aluguel", "Moradia" -> Icons.Default.Home
    "Internet", "Contas" -> Icons.Default.Wifi
    "Freelance", "Renda extra" -> Icons.Default.CardGiftcard
    "Lazer" -> Icons.Default.CardGiftcard
    "Saúde" -> Icons.Default.List
    else -> Icons.Default.List
}

fun gerarTransacoesRecentes(): List<Transacao> {
    // No Dashboard mostramos só as 3 transações mais recentes
    return gerarTodasTransacoes().take(3)
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MyApplicationTheme {
        DashboardScreen()
    }
}
