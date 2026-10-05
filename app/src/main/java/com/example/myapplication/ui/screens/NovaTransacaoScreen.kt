package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TransacaoManager
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.MensagemErro
import com.example.myapplication.ui.components.coresCampos
import com.example.myapplication.ui.components.dataDeHoje
import com.example.myapplication.ui.components.iconeDaCategoria
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.corDespesa
import com.example.myapplication.ui.theme.corReceita
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTransacaoScreen(
    voltar: () -> Unit
) {
    var tipoSelecionado by remember { mutableStateOf(TransacaoManager.DESPESA) }
    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    var categoriaSelecionada by remember { mutableStateOf(TransacaoManager.categoriasDespesa.first()) }
    var dataSelecionada by remember { mutableStateOf(dataDeHoje()) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val categorias = if (tipoSelecionado == TransacaoManager.DESPESA)
        TransacaoManager.categoriasDespesa
    else TransacaoManager.categoriasReceita

    val valorConvertido = valor.replace(",", ".").toDoubleOrNull()
    val valorInvalido = valor.isNotEmpty() && (valorConvertido == null || valorConvertido <= 0.0)
    val descricaoInvalida = descricao.isNotEmpty() && descricao.trim().length < 3
    val formularioValido = valorConvertido != null && valorConvertido > 0.0 && descricao.trim().length >= 3

    val corDoTipo = if (tipoSelecionado == TransacaoManager.DESPESA) corDespesa() else corReceita()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
        ) {
            OpcaoTipo(
                texto = "Despesa",
                selecionado = tipoSelecionado == TransacaoManager.DESPESA,
                cor = corDespesa(),
                onClick = {
                    tipoSelecionado = TransacaoManager.DESPESA
                    categoriaSelecionada = TransacaoManager.categoriasDespesa.first()
                },
                modifier = Modifier.weight(1f)
            )
            OpcaoTipo(
                texto = "Receita",
                selecionado = tipoSelecionado == TransacaoManager.RECEITA,
                cor = corReceita(),
                onClick = {
                    tipoSelecionado = TransacaoManager.RECEITA
                    categoriaSelecionada = TransacaoManager.categoriasReceita.first()
                },
                modifier = Modifier.weight(1f)
            )
        }

        CartaoPadrao {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (tipoSelecionado == TransacaoManager.DESPESA) "Quanto você gastou?" else "Quanto você recebeu?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "R$",
                        style = MaterialTheme.typography.headlineSmall,
                        color = corDoTipo
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = valor,
                        onValueChange = { novoValor ->
                            val filtrado = novoValor.filter { it.isDigit() || it == ',' }
                            if (filtrado.count { it == ',' } <= 1) valor = filtrado
                        },
                        placeholder = { Text("0,00", style = MaterialTheme.typography.headlineSmall) },
                        textStyle = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = valorInvalido,
                        shape = RoundedCornerShape(14.dp),
                        colors = coresCampos(),
                        modifier = Modifier.width(180.dp)
                    )
                }
                if (valorInvalido) {
                    Spacer(Modifier.height(6.dp))
                    MensagemErro("Informe um valor maior que zero.")
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                placeholder = { Text("Ex: Almoço, Mercado, Uber") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                singleLine = true,
                isError = descricaoInvalida,
                shape = RoundedCornerShape(14.dp),
                colors = coresCampos(),
                modifier = Modifier.fillMaxWidth()
            )
            if (descricaoInvalida) {
                MensagemErro("A descrição deve conter pelo menos 3 caracteres.")
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoriaSelecionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoria") },
                leadingIcon = { Icon(iconeDaCategoria(categoriaSelecionada), contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                shape = RoundedCornerShape(14.dp),
                colors = coresCampos(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        leadingIcon = { Icon(iconeDaCategoria(categoria), contentDescription = null) },
                        onClick = {
                            categoriaSelecionada = categoria
                            expandido = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = dataSelecionada,
            onValueChange = {},
            label = { Text("Data") },
            readOnly = true,
            enabled = false,
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
            shape = RoundedCornerShape(14.dp),
            colors = coresCampos(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mostrarCalendario = true }
        )

        if (mostrarCalendario) {
            val datePicker = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarCalendario = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            formato.timeZone = TimeZone.getTimeZone("UTC")
                            datePicker.selectedDateMillis?.let {
                                dataSelecionada = formato.format(Date(it))
                            }
                            mostrarCalendario = false
                        }
                    ) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = datePicker)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                TransacaoManager.adicionar(
                    descricao = descricao,
                    categoria = categoriaSelecionada,
                    data = dataSelecionada,
                    valor = valorConvertido ?: 0.0,
                    tipo = tipoSelecionado
                )
                Toast.makeText(context, "Transação salva!", Toast.LENGTH_SHORT).show()
                voltar()
            },
            enabled = formularioValido,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text("Salvar transação")
        }
    }
}

@Composable
private fun OpcaoTipo(
    texto: String,
    selecionado: Boolean,
    cor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) cor else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (selecionado) MaterialTheme.colorScheme.onPrimary else cor)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = if (selecionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NovaTransacaoScreenPreview() {
    MyApplicationTheme {
        NovaTransacaoScreen(voltar = {})
    }
}
