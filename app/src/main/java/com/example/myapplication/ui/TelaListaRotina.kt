package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  TelaListaRotina.kt — pacote ui.rotinas     ║
// ╚══════════════════════════════════════════════╝

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodel.ListaRotinasViewModel


@Composable
fun TelaListaRotinas(
    navController: NavHostController,
    viewModel: ListaRotinasViewModel
) {

    val begeFundo = Color(0xFFF3EFE0)

    // A tela só observa o estado que o ViewModel publica.
    // A lista vem do banco pelo Flow e atualiza sozinha.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = begeFundo
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Gerenciador de tempo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,

                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 24.dp
                )
            )


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = uiState.listaDeRotinas,
                    key = { rotina ->
                        rotina.id
                    }
                ) { rotina ->

                    CardRotina(
                        idRotina = rotina.id,
                        titulo = rotina.nomeRotina,
                        descricao = "${rotina.tempoMinutosRotina} min",
                        navController = navController,
                        aoClicarRemover = {
                            // abre a confirmação (quem guarda isso é o ViewModel)
                            viewModel.onDelete(rotina)
                        }
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            Button(
                onClick = {

                    navController.navigate(
                        RotaAbas.TelaCriacao
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF388E3C)
                )
            ) {

                Text(
                    text = "+ Nova rotina",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }


    // Confirmação de exclusão
    val rotinaParaExcluir = uiState.rotinaParaExcluir

    if (rotinaParaExcluir != null) {

        AlertDialog(

            onDismissRequest = {
                viewModel.onCancelDelete()
            },

            title = {
                Text(
                    text = "Excluir rotina?"
                )
            },

            text = {
                Text(
                    text = "A rotina \"${rotinaParaExcluir.nomeRotina}\" e todas as atividades dela serão removidas."
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        // Remove a rotina e as atividades dela
                        // DO BANCO (o ViewModel chama o Repository)
                        viewModel.onConfirmDelete()
                    }
                ) {

                    Text(
                        text = "Excluir",
                        color = Color.Red
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        viewModel.onCancelDelete()
                    }
                ) {

                    Text(
                        text = "Cancelar"
                    )
                }
            }
        )
    }
}


@Composable
fun CardRotina(
    idRotina: Int,
    titulo: String,
    descricao: String,
    navController: NavHostController,
    aoClicarRemover: () -> Unit
) {

    val vermelhoCard = Color(0xFFFF5252)


    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = vermelhoCard
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {


            // Área do texto da rotina
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {

                        navController.navigate(
                            "${RotaAbas.TelaRotina}/$idRotina"
                        )
                    }
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = descricao,
                    fontSize = 14.sp,
                    color = Color.White.copy(
                        alpha = 0.9f
                    )
                )
            }


            Spacer(
                modifier = Modifier.size(12.dp)
            )


            // Botão de excluir
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {

                        aoClicarRemover()
                    },

                shape = CircleShape,

                color = Color.White.copy(
                    alpha = 0.4f
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,

                        contentDescription = "Remover rotina",

                        tint = Color.White,

                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun AppBar(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        horizontalArrangement = Arrangement.SpaceAround,

        verticalAlignment = Alignment.CenterVertically
    ) {


        NavItem(
            label = "Rotinas",

            icon = Icons.Default.Home,

            isSelected = selectedTab == 0,

            onClick = {
                onTabSelected(0)
            },

            modifier = Modifier.weight(1f)
        )


        NavItem(
            label = "Progresso",

            icon = Icons.AutoMirrored.Filled.List,

            isSelected = selectedTab == 1,

            onClick = {
                onTabSelected(1)
            },

            modifier = Modifier.weight(1f)
        )


        NavItem(
            label = "Perfil",

            icon = Icons.Default.Person,

            isSelected = selectedTab == 2,

            onClick = {
                onTabSelected(2)
            },

            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun NavItem(
    label: String,

    icon: ImageVector,

    isSelected: Boolean,

    onClick: () -> Unit,

    modifier: Modifier = Modifier
) {

    val activeColor = Color(0xFF388E3C)

    val inactiveColor = Color(0xFF8D8D8D)


    val contentColor =
        if (isSelected) {
            activeColor
        } else {
            inactiveColor
        }


    Column(

        modifier = modifier.clickable(
            onClick = onClick
        ),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Icon(

            imageVector = icon,

            contentDescription = label,

            tint = contentColor,

            modifier = Modifier.size(24.dp)
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Text(

            text = label,

            color = contentColor,

            fontSize = 12.sp,

            fontWeight =
                if (isSelected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
        )
    }
}
