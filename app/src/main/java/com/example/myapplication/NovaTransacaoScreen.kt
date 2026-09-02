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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTransacaoScreen(
    modifier: Modifier = Modifier,
    aoClicarVoltar: () -> Unit = {},
    aoSalvarTransacao: () -> Unit = {}
) {

    val coresInputs = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    )

    var tipoSelecionado by remember { mutableStateOf("despesa") }
    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    var categoriaSelecionada by remember { mutableStateOf("Alimentação") }
    val categorias = listOf("Alimentação", "Transporte", "Moradia", "Lazer", "Saúde", "Contas", "Renda")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------- Cabeçalho ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier.clickable { aoClicarVoltar() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Nova transação",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Botões segmentados Despesa / Receita ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = if (tipoSelecionado == "despesa") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { tipoSelecionado = "despesa" }
            ) {
                Text(
                    text = "Despesa",
                    color = if (tipoSelecionado == "despesa") MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 14.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            Surface(
                color = if (tipoSelecionado == "receita") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { tipoSelecionado = "receita" }
            ) {
                Text(
                    text = "Receita",
                    color = if (tipoSelecionado == "receita") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 14.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Campo de valor ----------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VALOR DA TRANSAÇÃO",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "R$ ",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = valor,
                    onValueChange = { novoValor -> valor = novoValor.filter { it.isDigit() } },
                    placeholder = { Text("0,00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = coresInputs,
                    modifier = Modifier.width(160.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Descrição ----------
        Text(text = "Descrição", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            placeholder = { Text("Ex: Almoço, Mercado, Uber") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = coresInputs
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Categoria (dropdown, mesmo padrão do FormularioAula) ----------
        Text(text = "Categoria", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoriaSelecionada,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                colors = coresInputs
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        onClick = {
                            categoriaSelecionada = categoria
                            expandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Data ----------
        // OBS: DatePickerDialog ainda não foi aprofundado em aula, então por enquanto
        // mostramos a data como texto fixo (somente leitura), pronto para ser plugado
        // a um seletor de data real quando esse conteúdo avançar.
        Text(text = "Data", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = "01 de setembro, 2026",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Selecionar data")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = coresInputs
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ---------- Botão salvar ----------
        Button(
            onClick = { aoSalvarTransacao() /* Persistência de dados será tratada em aula futura */ },
            enabled = valor.isNotEmpty() && descricao.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Salvar transação", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NovaTransacaoScreenPreview() {
    MyApplicationTheme {
        NovaTransacaoScreen()
    }
}
