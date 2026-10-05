package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.ui.components.CartaoPadrao
import com.example.myapplication.ui.components.MensagemErro
import com.example.myapplication.ui.components.coresCampos
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CadastroScreen(
    irLogin: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val nomeInvalido = nome.isNotEmpty() && nome.trim().length < 3
    val emailInvalido = email.isNotEmpty() && !email.contains("@")
    val senhaInvalida = senha.isNotEmpty() && senha.length < 6
    val formularioValido = nome.trim().length >= 3 && email.contains("@") && senha.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = "Criar conta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Leva menos de um minuto. Depois é só registrar seus gastos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(28.dp))

        CartaoPadrao {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    isError = nomeInvalido,
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampos(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (nomeInvalido) {
                    MensagemErro("O nome deve conter pelo menos 3 caracteres.")
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    isError = emailInvalido,
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampos(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailInvalido) {
                    MensagemErro("Digite um e-mail válido, com @.")
                }

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = if (senhaVisivel)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (senhaVisivel)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff
                        val descricao = if (senhaVisivel) "Esconder senha"
                        else "Mostrar senha"
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(imageVector = image, contentDescription = descricao)
                        }
                    },
                    singleLine = true,
                    isError = senhaInvalida,
                    shape = RoundedCornerShape(14.dp),
                    colors = coresCampos(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (senhaInvalida) {
                    MensagemErro("A senha deve ter pelo menos 6 caracteres.")
                }

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (AuthManager.cadastrar(nome, email, senha)) {
                            Toast.makeText(
                                context, "Cadastro realizado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            irLogin()
                        } else {
                            Toast.makeText(
                                context, "Este e-mail já está cadastrado!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = formularioValido,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Criar conta")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = irLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Já tem uma conta? Entrar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    MyApplicationTheme {
        CadastroScreen(irLogin = {})
    }
}
