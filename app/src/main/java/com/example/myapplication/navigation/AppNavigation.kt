package com.example.myapplication.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myapplication.ui.screens.CadastroScreen
import com.example.myapplication.ui.screens.CategoriasScreen
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.DetalheTransacaoScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.NovaTransacaoScreen
import com.example.myapplication.ui.screens.TransacoesScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(
                irLogin = {
                    navController.navigate(Dashboard) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                irCadastro = {
                    navController.navigate(Cadastro)
                }
            )
        }

        composable<Cadastro> {
            CadastroScreen(
                irLogin = { navController.popBackStack() }
            )
        }

        composable<Dashboard> {
            DashboardScreen(
                irParaTransacoes = { navController.navigate(Transacoes) },
                irParaDetalhes = { id -> navController.navigate(DetalheTransacao(id)) }
            )
        }

        composable<Transacoes> {
            TransacoesScreen(
                irParaDetalhes = { id -> navController.navigate(DetalheTransacao(id)) }
            )
        }

        composable<Categorias> {
            CategoriasScreen()
        }

        composable<NovaTransacao>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            }
        ) {
            NovaTransacaoScreen(
                voltar = { navController.popBackStack() }
            )
        }

        composable<DetalheTransacao>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }
        ) { back ->
            val rota = back.toRoute<DetalheTransacao>()
            DetalheTransacaoScreen(
                transacaoId = rota.id,
                irHome = {
                    navController.popBackStack(
                        route = Dashboard,
                        inclusive = false
                    )
                },
                voltar = { navController.popBackStack() }
            )
        }
    }
}
