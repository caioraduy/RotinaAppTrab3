package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  TelaMetas.kt — pacote ui.metas             ║
// ╚══════════════════════════════════════════════╝

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.data.local.Meta
import com.example.myapplication.viewmodel.MetasViewModel

@Composable
fun TelaMetas(
    navController: NavHostController,
    viewModel: MetasViewModel
) {

    // Antes: várias variáveis remember soltas + DAO dentro da tela
    // Agora: tudo vem do UiState do MetasViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                            text = "${uiState.metasConcluidas} de ${uiState.totalMetas} metas concluídas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = if (uiState.totalMetas == 0) {
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
                        viewModel.onNovaMeta()
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
                items = uiState.listaDeMetas,
                key = { it.id }
            ) { meta ->


                CardMeta(
                    meta = meta,

                    onClick = {
                        viewModel.onSelect(meta)
                    },

                    onCheck = {
                        // concluir / desmarcar: o ViewModel atualiza no banco
                        viewModel.onCheck(meta)
                    },

                    onEdit = {
                        // preenche o diálogo com os dados da meta
                        viewModel.onEdit(meta)
                    },

                    onDelete = {
                        viewModel.onDelete(meta)
                    }
                )


            }
        }

        uiState.metaSelecionada?.let { meta ->

            AlertDialog(
                onDismissRequest = {
                    viewModel.onDismissDetalhes()
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
                            viewModel.onDismissDetalhes()
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

    if (uiState.mostrarDialogo) {

        DialogoNovaMeta(

            nome = uiState.nome,
            descricao = uiState.descricao,
            titulo = uiState.tituloDialogo,
            textoBotao = uiState.textoBotao,

            aoMudarNome = {
                viewModel.onNomeChange(it)
            },

            aoMudarDescricao = {
                viewModel.onDescricaoChange(it)
            },

            onDismiss = {
                viewModel.onDismissDialogo()
            },

            onConfirm = {
                // o ViewModel decide: insere (nova) ou atualiza (editando)
                viewModel.onSave()
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
    titulo: String,
    textoBotao: String,
    aoMudarNome: (String) -> Unit,
    aoMudarDescricao: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = titulo,
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
                    // sem nome, o ViewModel não salva
                    // (o diálogo continua aberto)
                    onConfirm()
                }
            ) {

                Text(textoBotao)
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
