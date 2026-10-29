
package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.Meta
import com.example.myapplication.ui.theme.registrarMetaNoHistorico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaMetas(
    navController: NavHostController
) {

    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    // ==========================================
    // EDIÇÃO (mesmo padrão do exemplo de Filmes)
    // ==========================================

    var estouEditando by remember { mutableStateOf(false) }
    var metaEmEdicao by remember {
        mutableStateOf<Meta?>(null)
    }

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    // Antes: lista global em memória (listaMetas no MetasViewModel)
    // Agora: a lista vem do banco Room
    var metas by remember {
        mutableStateOf<List<Meta>>(emptyList())
    }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val metaDAO = db.metaDAO()


    // Busca as metas do banco assim que a tela abre
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            metas = buscarMetas(metaDAO)
        }
    }

    val total = metas.size

    val concluidas = metas.count {
        it.concluida
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EFE0))
            .padding(horizontal = 20.dp)
    ) {

        // ==========================================
        // TOPO
        // ==========================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
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
                text = "Minhas metas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )

        var metaSelecionada by remember { mutableStateOf<Meta?>(null) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ==========================================
            // RESUMO
            // ==========================================

            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "$concluidas de $total metas concluídas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = if (total == 0) {
                                "Crie sua primeira meta!"
                            } else {
                                "Continue avançando!"
                            },
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }


            // ==========================================
            // NOVA META
            // ==========================================

            item {

                Button(
                    onClick = {
                        // abre o diálogo limpo (modo adicionar)
                        nome = ""
                        descricao = ""
                        estouEditando = false
                        metaEmEdicao = null

                        mostrarDialogo = true
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF388E3C)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text(
                        text = "Nova meta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            // ==========================================
            // METAS
            // ==========================================

            items(
                items = metas,
                key = { it.id }
            ) { meta ->


                CardMeta(
                    meta = meta,

                    onClick = {
                        metaSelecionada = meta
                    },

                    onCheck = {

                        // Concluir / desmarcar: atualiza no banco
                        CoroutineScope(Dispatchers.IO).launch {
                            val metaTemp = meta.copy(concluida = !meta.concluida)
                            atualizarMeta(metaTemp, metaDAO)
                            metas = buscarMetas(metaDAO)
                        }

                        // Só registra no histórico quando CONCLUI
                        if (!meta.concluida) {
                            registrarMetaNoHistorico(
                                meta.nomeMeta
                            )
                        }
                    },

                    onEdit = {
                        // preenche o diálogo com os dados da meta
                        nome = meta.nomeMeta
                        descricao = meta.descricaoMeta
                        estouEditando = true
                        metaEmEdicao = meta

                        mostrarDialogo = true
                    },

                    onDelete = {
                        CoroutineScope(Dispatchers.IO).launch {
                            deletarMeta(meta, metaDAO)
                            metas = buscarMetas(metaDAO)
                        }
                    }
                )


            }
        }

        metaSelecionada?.let { meta ->

            AlertDialog(
                onDismissRequest = {
                    metaSelecionada = null
                },

                title = {
                    Text(
                        text = meta.nomeMeta,
                        fontWeight = FontWeight.Bold
                    )
                },

                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = if (meta.descricaoMeta.isBlank()) {
                                "Sem descrição."
                            } else {
                                meta.descricaoMeta
                            }
                        )

                        HorizontalDivider()

                        Text(
                            text = "Status: ${
                                if (meta.concluida) {
                                    "Concluída"
                                } else {
                                    "Em andamento"
                                }
                            }",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ID da meta: ${meta.id}"
                        )
                    }
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            metaSelecionada = null
                        }
                    ) {
                        Text("Fechar")
                    }
                }
            )
        }

    }


    // ==========================================
    // DIALOGO (adicionar OU atualizar)
    // ==========================================

    if (mostrarDialogo) {

        DialogoNovaMeta(

            nome = nome,
            descricao = descricao,
            estouEditando = estouEditando,

            aoMudarNome = {
                nome = it
            },

            aoMudarDescricao = {
                descricao = it
            },

            onDismiss = {
                mostrarDialogo = false
                estouEditando = false
                metaEmEdicao = null
            },

            onConfirm = {

                CoroutineScope(Dispatchers.IO).launch {

                    if (estouEditando){
                        val metaTemp = metaEmEdicao!!.copy(
                            nomeMeta = nome.trim(),
                            descricaoMeta = descricao.trim()
                        )
                        atualizarMeta(metaTemp, metaDAO)
                        estouEditando = false
                    }else{
                        inserirMeta(nome.trim(), descricao.trim(), metaDAO)
                    }

                    nome = ""
                    descricao = ""

                    metaEmEdicao = null
                    metas = buscarMetas(metaDAO)
                }

                mostrarDialogo = false
            }
        )
    }
}


// ============================================================
// CARD DA META
// ============================================================

@Composable
fun CardMeta(
    meta: Meta,
    onClick: () -> Unit,
    onCheck: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

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

            Checkbox(
                checked = meta.concluida,

                onCheckedChange = {
                    onCheck()
                }
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = meta.nomeMeta,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (meta.descricaoMeta.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = meta.descricaoMeta,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }


            // EDITAR
            IconButton(
                onClick = onEdit
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFF388E3C)
                )
            }


            // EXCLUIR
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


// ============================================================
// DIALOGO NOVA META / EDITAR META
// ============================================================

@Composable
fun DialogoNovaMeta(
    nome: String,
    descricao: String,
    estouEditando: Boolean,
    aoMudarNome: (String) -> Unit,
    aoMudarDescricao: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = if (estouEditando) "Editar meta" else "Nova meta",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value = nome,

                    onValueChange = {
                        aoMudarNome(it)
                    },

                    label = {
                        Text("Nome da meta")
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                OutlinedTextField(
                    value = descricao,

                    onValueChange = {
                        aoMudarDescricao(it)
                    },

                    label = {
                        Text("Descrição")
                    },

                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            TextButton(
                onClick = {

                    if (nome.isNotBlank()) {

                        onConfirm()
                    }
                }
            ) {

                if (estouEditando) Text("Atualizar")
                else Text("Adicionar")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancelar")
            }
        }
    )
}

