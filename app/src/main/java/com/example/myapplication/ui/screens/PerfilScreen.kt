package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.MensagemErro
import com.example.myapplication.ui.components.coresCampos
import com.example.myapplication.ui.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    irLogin: () -> Unit,
    voltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }
    LaunchedEffect(viewModel.contaExcluida) {
        if (viewModel.contaExcluida) {
            irLogin()
        }
    }

    var novaSenha by remember { mutableStateOf("") }
    var confirmarExclusao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        viewModel.mensagem?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (viewModel.carregando && viewModel.usuario == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        val usuario = viewModel.usuario
        if (usuario != null) {
            CartaoPadrao {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Dados da conta",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = viewModel.nome,
                        onValueChange = viewModel::mudarNome,
                        label = { Text("Nome") },
                        singleLine = true,
                        enabled = !viewModel.carregando,
                        shape = RoundedCornerShape(14.dp),
                        colors = coresCampos(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = usuario.email,
                        onValueChange = {},
                        label = { Text("E-mail") },
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = coresCampos(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = novaSenha,
                        onValueChange = { if (it.length <= 128) novaSenha = it },
                        label = { Text("Nova senha (opcional)") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        enabled = !viewModel.carregando,
                        shape = RoundedCornerShape(14.dp),
                        colors = coresCampos(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (viewModel.carregando) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Button(
                            onClick = {
                                viewModel.salvar(novaSenha)
                                novaSenha = ""
                            },
                            enabled = !viewModel.carregando,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("Salvar alterações")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                SessaoUsuario.encerrar()
                irLogin()
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Sair da conta")
        }

        OutlinedButton(
            onClick = { confirmarExclusao = true },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Excluir minha conta")
        }
    }

    if (confirmarExclusao) {
        AlertDialog(
            onDismissRequest = { confirmarExclusao = false },
            title = { Text("Excluir conta?") },
            text = { Text("Essa ação não pode ser desfeita. Sua conta será removida permanentemente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarExclusao = false
                        viewModel.excluirConta()
                    },
                    enabled = !viewModel.carregando
                ) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarExclusao = false }) { Text("Cancelar") }
            }
        )
    }
}
