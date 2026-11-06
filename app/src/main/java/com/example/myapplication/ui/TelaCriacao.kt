package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  TelaCriacao.kt — pacote ui.rotinas         ║
// ╚══════════════════════════════════════════════╝

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodel.CriacaoRotinaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriacao(
    navController: NavHostController,
    viewModel: CriacaoRotinaViewModel
) {

    val contexto = LocalContext.current

    val begeFundo = Color(0xFFF3EFE0)
    val verdeCampo = Color(0xFF38B560)
    val verdeTexto = Color(0xFF7D8C7A)
    val vermelho = Color(0xFFFF5252)

    // Tudo que a tela desenha vem do UiState do ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // Avisos (Toast) que o ViewModel pede para mostrar
    LaunchedEffect(uiState.mensagem) {

        val mensagem = uiState.mensagem

        if (mensagem != null) {

            Toast.makeText(
                contexto,
                mensagem,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.onMensagemMostrada()
        }
    }


    // Quando o ViewModel termina de salvar, volta para a tela anterior
    LaunchedEffect(uiState.rotinaSalva) {

        if (uiState.rotinaSalva) {
            navController.popBackStack()
        }
    }


    Scaffold(
        containerColor = begeFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.idRotinaEditando != null) "Editar rotina" else "Nova rotina",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = begeFundo
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))


            // Título da rotina
            // (o id da rotina nova é gerado pelo banco ao salvar)
            Text(
                text = if (uiState.idRotinaEditando != null) "Rotina #${uiState.idRotinaEditando}" else "Rotina nova",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))


            // Nome da rotina
            CampoTexto(
                valor = uiState.nomeRotina,
                aoMudar = {
                    viewModel.onNomeRotinaChange(it)
                },
                placeholder = "Nome da rotina",
                cor = verdeCampo
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Horários
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Início",
                        fontSize = 12.sp,
                        color = verdeTexto
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CampoTexto(
                        valor = uiState.inicio,
                        aoMudar = {
                            viewModel.onInicioChange(it)
                        },
                        placeholder = "08:00",
                        cor = verdeCampo,
                        somenteNumeros = true
                    )
                }


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Fim",
                        fontSize = 12.sp,
                        color = verdeTexto
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CampoTexto(
                        valor = uiState.fim,
                        aoMudar = {
                            viewModel.onFimChange(it)
                        },
                        placeholder = "09:00",
                        cor = verdeCampo,
                        somenteNumeros = true
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "Adicionar atividade",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))


            // Nome + duração + botão
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    CampoTexto(
                        valor = uiState.nomeAtividade,
                        aoMudar = {
                            viewModel.onNomeAtividadeChange(it)
                        },
                        placeholder = "Nome atividade",
                        cor = verdeCampo
                    )

                    CampoTexto(
                        valor = uiState.duracaoAtividade,
                        aoMudar = {
                            viewModel.onDuracaoAtividadeChange(it)
                        },
                        placeholder = "Duração (min)",
                        cor = verdeCampo,
                        somenteNumeros = true
                    )
                }


                // Botão adicionar
                // (o ViewModel valida e manda o aviso "Atividade adicionada" ou
                // "Preencha nome e duração corretamente")
                IconButton(
                    onClick = {
                        viewModel.onAddAtividade()
                    },

                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            verdeCampo,
                            RoundedCornerShape(12.dp)
                        )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar atividade",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))


            // Título da lista
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Atividades",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${uiState.atividades.size} item(ns)",
                    color = verdeTexto
                )
            }


            Spacer(modifier = Modifier.height(8.dp))


            // LISTA DO RASCUNHO (vem do UiState)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = uiState.atividades,
                    key = {
                        it.id
                    }
                ) { atividade ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE2DDD0)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),

                            horizontalArrangement =
                                Arrangement.SpaceBetween,

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = atividade.nomeAtividade,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "${atividade.duracaoMinutos.toInt()} minutos",
                                    color = verdeTexto,
                                    fontSize = 14.sp
                                )
                            }


                            // REMOVER
                            IconButton(
                                onClick = {
                                    viewModel.onDeleteAtividade(
                                        atividade.id
                                    )
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover atividade",
                                    tint = vermelho
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))


            // SALVAR ROTINA
            // (quem valida e salva no banco é o ViewModel)
            Button(
                onClick = {
                    viewModel.onSave()
                },

                enabled = !uiState.salvando,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = vermelho
                ),

                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "Salvar rotina",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
private fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    placeholder: String,
    cor: Color,
    somenteNumeros: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = cor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            if (valor.isEmpty()) {

                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }

            BasicTextField(
                value = valor,

                onValueChange = aoMudar,

                singleLine = true,

                textStyle = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),

                cursorBrush = SolidColor(Color.White),

                keyboardOptions = KeyboardOptions(
                    keyboardType =
                        if (somenteNumeros)
                            KeyboardType.Number
                        else
                            KeyboardType.Text,

                    capitalization =
                        KeyboardCapitalization.Sentences
                ),

                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
