package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MeuBolsoApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Controla a navegação entre as 5 telas do app usando apenas estado local
 * (var telaAtual + when), sem Navigation Component, já que esse conteúdo
 * ainda não foi apresentado em aula.
 */
@Composable
fun MeuBolsoApp(modifier: Modifier = Modifier) {

    // Nome da tela atualmente exibida. Alterna entre:
    // "login", "dashboard", "transacoes", "novaTransacao", "categorias"
    var telaAtual by remember { mutableStateOf("login") }

    when (telaAtual) {
        "login" -> LoginScreen(
            modifier = modifier,
            aoEntrar = { telaAtual = "dashboard" }
        )

        "dashboard" -> DashboardScreen(
            modifier = modifier,
            aoClicarTransacoes = { telaAtual = "transacoes" },
            aoClicarAdicionar = { telaAtual = "novaTransacao" },
            aoClicarCategorias = { telaAtual = "categorias" }
        )

        "transacoes" -> TransacoesScreen(
            modifier = modifier,
            aoClicarInicio = { telaAtual = "dashboard" },
            aoClicarAdicionar = { telaAtual = "novaTransacao" },
            aoClicarCategorias = { telaAtual = "categorias" }
        )

        "novaTransacao" -> NovaTransacaoScreen(
            modifier = modifier,
            aoClicarVoltar = { telaAtual = "dashboard" },
            aoSalvarTransacao = { telaAtual = "dashboard" }
        )

        "categorias" -> CategoriasScreen(
            modifier = modifier,
            aoClicarInicio = { telaAtual = "dashboard" },
            aoClicarTransacoes = { telaAtual = "transacoes" },
            aoClicarAdicionar = { telaAtual = "novaTransacao" }
        )
    }
}
