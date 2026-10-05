package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

// =====================================================================
// Paleta do Meu Bolso
// Linguagem visual escura e premium: fundos profundos em camadas,
// texto quase-branco, acento índigo/periwinkle e detalhe em dourado.
// As cores de receita/despesa seguem o padrão verde/vermelho.
// (Mesma técnica vista em aula: cada cor é um Color(0xFF......).)
// =====================================================================

// ---- Acento índigo (o "destaque" do app) ----
val IndigoProfundo = Color(0xFF5C6CFF) // acento principal no tema claro
val IndigoClaro    = Color(0xFF93A7FF) // acento principal no tema escuro

// ---- Dourado (acento secundário, usado com parcimônia) ----
val Ouro        = Color(0xFFD8B978)
val OuroEscuro  = Color(0xFF2A2103)

// ---- Tema claro ----
val DiaFundo         = Color(0xFFF5F6FB)
val DiaSuperficie    = Color(0xFFFFFFFF)
val DiaSuperficie2   = Color(0xFFEDEEF6)
val DiaContorno      = Color(0xFFDFE1EC)
val DiaContornoForte = Color(0xFFC6C9DB)
val DiaTexto         = Color(0xFF15161F)
val DiaTextoSuave    = Color(0xFF585A6B)
val DiaIndigoSuave   = Color(0xFFE5E8FF) // primaryContainer claro
val DiaIndigoTexto   = Color(0xFF1C2250)
val DiaDouradoSuave  = Color(0xFFF4E9CC)

// ---- Tema escuro (protagonista) ----
val NoiteFundo         = Color(0xFF0B0B0F)
val NoiteSuperficie    = Color(0xFF14141C)
val NoiteSuperficie2   = Color(0xFF1B1B25)
val NoiteSuperficie3   = Color(0xFF23232F)
val NoiteContorno      = Color(0xFF2A2A38)
val NoiteContornoForte = Color(0xFF3B3B4C)
val NoiteTexto         = Color(0xFFF5F5F0)
val NoiteTextoSuave    = Color(0xFFA4A4B0)
val NoiteTextoFraco    = Color(0xFF6A6A78)
val NoiteIndigoSuave   = Color(0xFF242A44) // primaryContainer escuro
val NoiteIndigoTexto   = Color(0xFFC9D0FF)

// ---- Cores semânticas de finanças ----
val ReceitaDia   = Color(0xFF1F9D63)
val DespesaDia   = Color(0xFFD1544E)
val ReceitaNoite = Color(0xFF7FD8B0)
val DespesaNoite = Color(0xFFFF8A8A)
