package com.example.myapplication.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.ui.screens.CadastroScreen
import com.example.myapplication.ui.screens.CategoriasScreen
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.DetalheTransacaoScreen
import com.example.myapplication.ui.screens.EditarTransacaoScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.NovaTransacaoScreen
import com.example.myapplication.ui.screens.PerfilScreen
import com.example.myapplication.ui.screens.TransacoesScreen
import com.example.myapplication.ui.viewmodel.PerfilViewModel
import com.example.myapplication.ui.viewmodel.TransacaoViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination
    val usuarioAtual = SessaoUsuario.usuarioId

    LaunchedEffect(destino, usuarioAtual) {
        val publica = destino?.hasRoute<Login>() == true ||
                destino?.hasRoute<Cadastro>() == true
        if (destino != null && !publica && usuarioAtual == null) {
            navController.navigate(Login) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(
                authViewModel = viewModel(),
                irLogin = {
                    navController.navigate(Dashboard) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                irCadastro = { navController.navigate(Cadastro) }
            )
        }

        composable<Cadastro> {
            CadastroScreen(
                authViewModel = viewModel(),
                irLogin = { navController.popBackStack() }
            )
        }

        composable<Dashboard> { entrada ->
            val viewModel: TransacaoViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) viewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            DashboardScreen(
                viewModel = viewModel,
                irParaTransacoes = { navController.navigate(Transacoes) },
                irParaDetalhes = { id -> navController.navigate(DetalheTransacao(id)) }
            )
        }

        composable<Transacoes> { entrada ->
            val viewModel: TransacaoViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) viewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            TransacoesScreen(
                viewModel = viewModel,
                irParaDetalhes = { id -> navController.navigate(DetalheTransacao(id)) }
            )
        }

        composable<Categorias> { entrada ->
            val viewModel: TransacaoViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) viewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            CategoriasScreen(viewModel = viewModel)
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
                viewModel = viewModel(),
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
                viewModel = viewModel(),
                irHome = {
                    navController.popBackStack(route = Dashboard, inclusive = false)
                },
                irEditar = { id -> navController.navigate(EditarTransacao(id)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<EditarTransacao>(
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
        ) { back ->
            val rota = back.toRoute<EditarTransacao>()
            EditarTransacaoScreen(
                transacaoId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Perfil> {
            val viewModel: PerfilViewModel = viewModel()
            PerfilScreen(
                viewModel = viewModel,
                irLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                voltar = { navController.popBackStack() }
            )
        }
    }
}
