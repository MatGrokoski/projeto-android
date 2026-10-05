package com.example.myapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.model.Transacao
import com.example.myapplication.ui.theme.corDespesa
import com.example.myapplication.ui.theme.corReceita

// Cartão padrão do app: cantos generosos, fundo de superfície e um contorno
// fino (em vez de sombra pesada), no estilo do design escuro premium.
@Composable
fun CartaoPadrao(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        content = content
    )
}

// Rótulo de seção em CAIXA ALTA com um traço antes — a "etiqueta" do design.
@Composable
fun RotuloSecao(texto: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun IconeCategoria(categoria: String, tipo: String, tamanho: Int = 44) {
    val cor = if (tipo == TransacaoManager.RECEITA) corReceita() else MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tamanho.dp)
            .clip(RoundedCornerShape((tamanho * 0.32).dp))
            .background(cor.copy(alpha = 0.14f))
    ) {
        Icon(
            imageVector = iconeDaCategoria(categoria),
            contentDescription = categoria,
            tint = cor,
            modifier = Modifier.size((tamanho * 0.5).dp)
        )
    }
}

@Composable
fun LinhaTransacao(
    transacao: Transacao,
    onClick: () -> Unit = {}
) {
    val ehReceita = transacao.tipo == TransacaoManager.RECEITA
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        IconeCategoria(categoria = transacao.categoria, tipo = transacao.tipo)

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transacao.descricao,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = transacao.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = (if (ehReceita) "+ " else "- ") + transacao.valor.emReais(),
            style = MaterialTheme.typography.titleMedium,
            color = if (ehReceita) corReceita() else MaterialTheme.colorScheme.onSurface
        )
    }
}

// Chip de filtro no estilo "tag": contorno fino quando inativo e um leve
// preenchimento de acento quando selecionado.
@Composable
fun ChipFiltro(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    val formato = RoundedCornerShape(50)
    val corFundo = if (selecionado) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val corBorda = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val corTexto = if (selecionado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = Modifier
            .clip(formato)
            .background(corFundo)
            .border(width = 1.dp, color = corBorda, shape = formato)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 9.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = corTexto
        )
    }
}

@Composable
fun CardIndicador(
    titulo: String,
    valor: String,
    detalhe: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = titulo.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = detalhe,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TituloSecao(
    texto: String,
    textoAcao: String? = null,
    onAcao: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        if (textoAcao != null) {
            TextButton(onClick = onAcao) {
                Text(textoAcao)
            }
        }
    }
}

// Marcador de tipo no estilo "tag": ponto colorido + texto dentro de uma
// pílula com contorno suave na cor da receita/despesa.
@Composable
fun MarcadorTipo(tipo: String) {
    val ehReceita = tipo == TransacaoManager.RECEITA
    val cor = if (ehReceita) corReceita() else corDespesa()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(cor.copy(alpha = 0.12f))
            .border(width = 1.dp, color = cor.copy(alpha = 0.35f), shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(cor)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = if (ehReceita) "Receita" else "Despesa",
            style = MaterialTheme.typography.labelMedium,
            color = cor
        )
    }
}

@Composable
fun coresCampos(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledBorderColor = MaterialTheme.colorScheme.outline,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
    disabledContainerColor = MaterialTheme.colorScheme.surface
)

@Composable
fun MensagemErro(texto: String) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 4.dp)
    )
}
