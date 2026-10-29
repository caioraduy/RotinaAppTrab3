package com.example.myapplication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.registrarRotinaNoHistorico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriacao(
    navController: androidx.navigation.NavHostController,
    viewModel: TelaCriacaoViewModel = viewModel()
) {

    val contexto = LocalContext.current

    val begeFundo = Color(0xFFF3EFE0)
    val verdeCampo = Color(0xFF38B560)
    val verdeTexto = Color(0xFF7D8C7A)
    val vermelho = Color(0xFFFF5252)

    val nomeRotina by viewModel.nomeRotina
    val inicio by viewModel.inicio
    val fim by viewModel.fim
    val nomeAtividade by viewModel.nomeAtividade
    val duracaoAtividade by viewModel.duracaoAtividade
    val idRotinaEditando by viewModel.idRotinaEditando

    val db = AppDatabase.getDatabase(contexto)
    val rotinaDAO = db.rotinaDAO()
    val atividadeDAO = db.atividadeDAO()

    // evita salvar duas vezes se clicar rápido no botão
    var salvando by remember {
        mutableStateOf(false)
    }


    Scaffold(
        containerColor = begeFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (idRotinaEditando != null) "Editar rotina" else "Nova rotina",
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
            // (o id da rotina nova agora é gerado pelo banco ao salvar)
            Text(
                text = if (idRotinaEditando != null) "Rotina #$idRotinaEditando" else "Rotina nova",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))


            // Nome da rotina
            CampoTexto(
                valor = nomeRotina,
                aoMudar = {
                    viewModel.alterarNomeRotina(it)
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
                        valor = inicio,
                        aoMudar = {
                            viewModel.alterarInicio(it)
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
                        valor = fim,
                        aoMudar = {
                            viewModel.alterarFim(it)
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
                        valor = nomeAtividade,
                        aoMudar = {
                            viewModel.alterarNomeAtividade(it)
                        },
                        placeholder = "Nome atividade",
                        cor = verdeCampo
                    )

                    CampoTexto(
                        valor = duracaoAtividade,
                        aoMudar = {
                            viewModel.alterarDuracaoAtividade(it)
                        },
                        placeholder = "Duração (min)",
                        cor = verdeCampo,
                        somenteNumeros = true
                    )
                }


                // Botão adicionar
                IconButton(
                    onClick = {

                        val adicionou =
                            viewModel.adicionarAtividade()

                        if (adicionou) {

                            Toast.makeText(
                                contexto,
                                "Atividade adicionada",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                contexto,
                                "Preencha nome e duração corretamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
                    text = "${viewModel.atividades.size} item(ns)",
                    color = verdeTexto
                )
            }


            Spacer(modifier = Modifier.height(8.dp))


            // LISTA REATIVA
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = viewModel.atividades,
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

                                    viewModel.removerAtividade(
                                        atividade.id
                                    )

                                    Toast.makeText(
                                        contexto,
                                        "Atividade removida",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            Button(
                onClick = {

                    if (nomeRotina.isBlank()) {

                        Toast.makeText(
                            contexto,
                            "Dê um nome à rotina",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        salvando = true

                        // Salva no banco fora da thread principal
                        CoroutineScope(Dispatchers.IO).launch {

                            val rotina =
                                viewModel.salvarRotina(rotinaDAO, atividadeDAO)

                            // Toast e navegação precisam rodar na thread principal
                            withContext(Dispatchers.Main) {

                                if (rotina != null) {

                                    Toast.makeText(
                                        contexto,
                                        "Rotina salva com ${viewModel.atividades.size} atividade(s)",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    registrarRotinaNoHistorico(
                                        rotina.nomeRotina
                                    )

                                    navController.popBackStack()

                                } else {

                                    Toast.makeText(
                                        contexto,
                                        "Erro ao salvar a rotina",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    salvando = false
                                }
                            }
                        }
                    }
                },

                enabled = !salvando,

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