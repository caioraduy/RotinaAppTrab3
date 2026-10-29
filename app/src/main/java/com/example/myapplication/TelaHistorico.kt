package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.Meta
import com.example.myapplication.ui.theme.Rotina
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaHistorico(
    navController: NavHostController,
    viewModel: HistoricoViewModel = viewModel()
) {

    val historicos = viewModel.historicos

    // Antes: listaRotinas e listaMetas (memória)
    // Agora: rotinas e metas vêm do banco Room
    var listaRotinas by remember {
        mutableStateOf<List<Rotina>>(emptyList())
    }

    var listaMetas by remember {
        mutableStateOf<List<Meta>>(emptyList())
    }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val rotinaDAO = db.rotinaDAO()
    val metaDAO = db.metaDAO()

    // Busca rotinas e metas do banco assim que a tela abre
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            listaRotinas = buscarRotinas(rotinaDAO)
            listaMetas = buscarMetas(metaDAO)
        }
    }

    val totalRotinas = listaRotinas.size

    val totalMetas = listaMetas.size

    val totalDias = historicos
        .map { it.data }
        .distinct()
        .size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EFE0))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ----------------------------------------------------
        // TOPO
        // ----------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar"
                )
            }

            Text(
                text = "Histórico",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ----------------------------------------------------
        // RESUMO
        // ----------------------------------------------------

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Seu resumo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    ResumoItem(
                        valor = totalRotinas.toString(),
                        titulo = "Rotinas"
                    )

                    ResumoItem(
                        valor = totalMetas.toString(),
                        titulo = "Metas"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ----------------------------------------------------
        // TÍTULO
        // ----------------------------------------------------

        Text(
            text = "Atividades recentes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // ----------------------------------------------------
        // HISTÓRICO
        // ----------------------------------------------------

        if (listaRotinas.isEmpty() && listaMetas.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Nenhuma atividade registrada",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Crie uma rotina ou meta para começar.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

        } else {

            // ROTINAS
            listaRotinas.forEach { rotina ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Rotina",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Text(
                                text = rotina.nomeRotina,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            // METAS
            listaMetas.forEach { meta ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Meta",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Text(
                                text = meta.nomeMeta,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (meta.concluida) {
                                Text(
                                    text = "Concluída",
                                    fontSize = 13.sp,
                                    color = Color(0xFF388E3C)
                                )
                            } else {
                                Text(
                                    text = "Em andamento",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}


// ============================================================
// ITEM DO RESUMO
// ============================================================

@Composable
fun ResumoItem(
    valor: String,
    titulo: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = valor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF388E3C)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = titulo,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}


// ============================================================
// CARD DO HISTÓRICO
// ============================================================

@Composable
fun CardHistorico(
    historico: com.example.myapplication.ui.theme.Historico,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = historico.data,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                if (historico.rotinaConcluida) {

                    Text(
                        text = "Rotina concluída",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = historico.nomeRotina,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                if (historico.metaConcluida) {

                    Text(
                        text = "Meta concluída",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = historico.nomeMeta,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            IconButton(
                onClick = onDelete
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}
