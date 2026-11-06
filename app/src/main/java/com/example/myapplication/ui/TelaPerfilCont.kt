package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  TelaPerfilCont.kt — pacote ui.perfil       ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPerfilCont(
    navController: NavHostController,
    viewModel: PerfilViewModel
) {

    val begeFundo = Color(0xFFF3EFE0)
    val verde = Color(0xFF388E3C)

    // perfil salvo + campos do formulário (nome e e-mail) vêm do UiState
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val perfil = uiState.perfil

    // Quando o ViewModel termina de salvar, volta para o perfil
    LaunchedEffect(uiState.perfilSalvo) {

        if (uiState.perfilSalvo) {
            viewModel.onPerfilSalvoMostrado()
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = begeFundo,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = if (perfil.nome.isBlank()) {
                            "Fazer cadastro"
                        } else {
                            "Editar perfil"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            // =================================
            // TÍTULO
            // =================================

            Text(
                text = if (perfil.nome.isBlank()) {
                    "Crie seu perfil"
                } else {
                    "Atualize seus dados"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = if (perfil.nome.isBlank()) {
                    "Preencha seus dados para começar."
                } else {
                    "Altere seus dados quando quiser."
                },
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // =================================
            // NOME
            // =================================

            Text(
                text = "Nome",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(
                value = uiState.nome,

                onValueChange = {
                    viewModel.onNomeChange(it)
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                singleLine = true,

                placeholder = {
                    Text("Digite seu nome")
                }
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // =================================
            // EMAIL
            // =================================

            Text(
                text = "E-mail",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(
                value = uiState.email,

                onValueChange = {
                    viewModel.onEmailChange(it)
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                singleLine = true,

                placeholder = {
                    Text("Digite seu e-mail")
                }
            )


            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // =================================
            // SALVAR
            // =================================

            Button(
                onClick = {
                    // Salva (insere ou atualiza) no banco.
                    // Se nome ou e-mail estiverem vazios, o ViewModel não salva.
                    viewModel.onSave()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {

                Text(
                    text = if (perfil.nome.isBlank()) {
                        "Criar meu perfil"
                    } else {
                        "Salvar alterações"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

