package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.IconeCategoria
import com.example.myapplication.ui.components.MarcadorTipo
import com.example.myapplication.ui.components.emReais
import com.example.myapplication.ui.components.rotuloData
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.corReceita

@Composable
fun DetalheTransacaoScreen(
    transacaoId: Int,
    irHome: () -> Unit,
    voltar: () -> Unit
) {
    val transacao = TransacaoManager.buscarPorId(transacaoId)
    val context = LocalContext.current

    if (transacao == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Essa transação não existe mais.",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = voltar) {
                Text("Voltar")
            }
        }
        return
    }

    val ehReceita = transacao.tipo == TransacaoManager.RECEITA

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        IconeCategoria(categoria = transacao.categoria, tipo = transacao.tipo, tamanho = 72)

        Spacer(Modifier.height(16.dp))

        Text(
            text = transacao.descricao,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = (if (ehReceita) "+ " else "- ") + transacao.valor.emReais(),
            style = MaterialTheme.typography.displaySmall,
            color = if (ehReceita) corReceita() else MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(8.dp))

        MarcadorTipo(tipo = transacao.tipo)

        Spacer(Modifier.height(28.dp))

        CartaoPadrao {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                LinhaDetalhe(rotulo = "Categoria", valor = transacao.categoria)
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LinhaDetalhe(rotulo = "Data", valor = rotuloData(transacao.data))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LinhaDetalhe(rotulo = "Tipo", valor = if (ehReceita) "Receita" else "Despesa")
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = irHome,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Voltar ao início")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                TransacaoManager.remover(transacao.id)
                Toast.makeText(context, "Transação excluída!", Toast.LENGTH_SHORT).show()
                voltar()
            },
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Text("Excluir transação", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun LinhaDetalhe(rotulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetalheTransacaoScreenPreview() {
    MyApplicationTheme {
        DetalheTransacaoScreen(transacaoId = 1, irHome = {}, voltar = {})
    }
}
