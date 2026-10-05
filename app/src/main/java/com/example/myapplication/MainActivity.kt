package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.BarraInferior
import com.example.myapplication.navigation.Menu
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rotaAtual = navBackStackEntry?.destination?.route ?: ""

                val mostrarMenu = rotaAtual.isNotEmpty() &&
                        !rotaAtual.contains("Login") &&
                        !rotaAtual.contains("Cadastro")

                val mostrarBarraInferior = rotaAtual.contains("Dashboard") ||
                        rotaAtual.contains("Transacoes") ||
                        rotaAtual.contains("Categorias")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        if (mostrarMenu) {
                            Menu(navController = navController, rotaAtual = rotaAtual)
                        }
                    },
                    bottomBar = {
                        if (mostrarBarraInferior) {
                            BarraInferior(navController = navController, rotaAtual = rotaAtual)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
