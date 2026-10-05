package com.example.myapplication.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navController: NavHostController,
    rotaAtual: String
) {
    val telaPrincipal = rotaAtual.contains("Dashboard") ||
            rotaAtual.contains("Transacoes") ||
            rotaAtual.contains("Categorias")

    val titulo = when {
        rotaAtual.contains("Dashboard") -> "Meu Bolso"
        rotaAtual.contains("Transacoes") -> "Transações"
        rotaAtual.contains("Categorias") -> "Resumo por categoria"
        rotaAtual.contains("NovaTransacao") -> "Nova transação"
        rotaAtual.contains("DetalheTransacao") -> "Detalhes"
        else -> ""
    }

    TopAppBar(
        title = {
            Text(text = titulo, style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            if (!telaPrincipal) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        },
        actions = {
            if (rotaAtual.contains("Dashboard")) {
                IconButton(onClick = {
                    AuthManager.sair()
                    navController.navigate(Login) {
                        popUpTo(Dashboard) { inclusive = true }
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sair"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun BarraInferior(
    navController: NavHostController,
    rotaAtual: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemMenu(
                    icone = Icons.Default.Home,
                    texto = "Início",
                    selecionado = rotaAtual.contains("Dashboard"),
                    onClick = {
                        navController.navigate(Dashboard) {
                            popUpTo(Dashboard) { inclusive = true }
                        }
                    }
                )

                ItemMenu(
                    icone = Icons.AutoMirrored.Filled.ReceiptLong,
                    texto = "Transações",
                    selecionado = rotaAtual.contains("Transacoes"),
                    onClick = {
                        navController.navigate(Transacoes) {
                            popUpTo(Dashboard)
                        }
                    }
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { navController.navigate(NovaTransacao) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar transação",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                ItemMenu(
                    icone = Icons.Default.PieChart,
                    texto = "Categorias",
                    selecionado = rotaAtual.contains("Categorias"),
                    onClick = {
                        navController.navigate(Categorias) {
                            popUpTo(Dashboard)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemMenu(
    icone: ImageVector,
    texto: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val cor = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !selecionado) { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(if (selecionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = texto,
                tint = cor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            color = cor
        )
    }
}
