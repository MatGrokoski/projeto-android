package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema claro: fundo bem claro, superfícies brancas e acento índigo profundo.
private val LightColorScheme = lightColorScheme(
    primary = IndigoProfundo,
    onPrimary = Color.White,
    primaryContainer = DiaIndigoSuave,
    onPrimaryContainer = DiaIndigoTexto,
    secondary = Ouro,
    onSecondary = OuroEscuro,
    secondaryContainer = DiaDouradoSuave,
    onSecondaryContainer = OuroEscuro,
    tertiary = ReceitaDia,
    onTertiary = Color.White,
    background = DiaFundo,
    onBackground = DiaTexto,
    surface = DiaSuperficie,
    onSurface = DiaTexto,
    surfaceVariant = DiaSuperficie2,
    onSurfaceVariant = DiaTextoSuave,
    surfaceContainerLowest = DiaSuperficie,
    surfaceContainerLow = DiaSuperficie,
    surfaceContainer = DiaSuperficie,
    surfaceContainerHigh = DiaSuperficie2,
    surfaceContainerHighest = DiaSuperficie2,
    outline = DiaContornoForte,
    outlineVariant = DiaContorno,
    error = DespesaDia,
    onError = Color.White
)

// Tema escuro (protagonista): fundo quase-preto em camadas, texto off-white,
// acento índigo claro. O acento claro sobre o fundo escuro cria o visual
// "invertido" premium (ex.: o cartão de saldo fica índigo com texto escuro).
private val DarkColorScheme = darkColorScheme(
    primary = IndigoClaro,
    onPrimary = NoiteFundo,
    primaryContainer = NoiteIndigoSuave,
    onPrimaryContainer = NoiteIndigoTexto,
    secondary = Ouro,
    onSecondary = OuroEscuro,
    secondaryContainer = NoiteSuperficie3,
    onSecondaryContainer = NoiteTexto,
    tertiary = ReceitaNoite,
    onTertiary = NoiteFundo,
    background = NoiteFundo,
    onBackground = NoiteTexto,
    surface = NoiteSuperficie,
    onSurface = NoiteTexto,
    surfaceVariant = NoiteSuperficie3,
    onSurfaceVariant = NoiteTextoSuave,
    surfaceContainerLowest = NoiteFundo,
    surfaceContainerLow = NoiteSuperficie,
    surfaceContainer = NoiteSuperficie2,
    surfaceContainerHigh = NoiteSuperficie3,
    surfaceContainerHighest = NoiteSuperficie3,
    outline = NoiteContornoForte,
    outlineVariant = NoiteContorno,
    error = DespesaNoite,
    onError = NoiteFundo
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun corReceita(): Color = if (isSystemInDarkTheme()) ReceitaNoite else ReceitaDia

@Composable
fun corDespesa(): Color = if (isSystemInDarkTheme()) DespesaNoite else DespesaDia
