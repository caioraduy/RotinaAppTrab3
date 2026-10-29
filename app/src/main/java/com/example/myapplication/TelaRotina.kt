
package com.example.myapplication

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.Atividade
import com.example.myapplication.ui.theme.Rotina
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaRotina(
    navController: NavHostController,
    idRotina: Int?
) {

    val fundoBege = Color(0xFFF3EFE0)

    /*
     * A tela recebe SÓ O ID pela navegação
     * e busca a rotina certa direto do banco.
     */
    var rotina by remember {
        mutableStateOf<Rotina?>(null)
    }

    /*
     * Somente as atividades pertencentes
     * à rotina selecionada (também vêm do banco).
     */
    var atividadesDaRotina by remember {
        mutableStateOf<List<Atividade>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val rotinaDAO = db.rotinaDAO()
    val atividadeDAO = db.atividadeDAO()

    /*
     * Assim que a tela abre, busca no banco:
     * - a rotina pelo id (consulta filtrada buscarPorId)
     * - as atividades dessa rotina (buscarPorRotina)
     */
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            if (idRotina != null) {
                rotina = buscarRotinaPorId(idRotina, rotinaDAO)
                atividadesDaRotina = buscarAtividadesDaRotina(idRotina, atividadeDAO)
            }

            carregando = false
        }
    }

    // cópia local para poder usar rotinaAtual.nomeRotina
    // depois do if (rotinaAtual != null)
    val rotinaAtual = rotina

    /*
     * Guarda quais atividades estão concluídas.
     *
     * A chave é o id da atividade.
     *
     * Exemplo:
     *
     * 1 -> true
     * 2 -> false
     * 3 -> true
     */
    val atividadesConcluidas = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fundoBege)
            .padding(20.dp)
    ) {

        /*
         * BOTÃO VOLTAR
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {

            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.popBackStack()
                    },

                shape = CircleShape,

                color = fundoBege
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )


        /*
         * CABEÇALHO DA ROTINA
         */
        if (carregando) {

            Text(
                text = "Carregando...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

        } else if (rotinaAtual != null) {

            Text(
                text = rotinaAtual.nomeRotina,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${rotinaAtual.tempoMinutosRotina} minutos",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Horário: ${rotinaAtual.horaInicio} às ${rotinaAtual.horaFim}",
                fontSize = 14.sp,
                color = Color.Gray
            )

        } else {

            Text(
                text = "Rotina não encontrada",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        /*
         * LISTA DE ATIVIDADES
         */
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = atividadesDaRotina,
                key = {
                    it.id
                }
            ) { atividade ->

                /*
                 * Verifica se a atividade está marcada.
                 */
                val concluida =
                    atividadesConcluidas[atividade.id] ?: false


                CardTarefa(
                    titulo = atividade.nomeAtividade,

                    descricao =
                        "Tempo: ${atividade.duracaoMinutos.toInt()} min",

                    isConcluida = concluida,

                    onCheckClick = {

                        /*
                         * INVERTE O ESTADO
                         *
                         * false -> true
                         * true -> false
                         */
                        atividadesConcluidas[
                            atividade.id
                        ] = !concluida
                    },

                    onDeleteClick = {

                        CoroutineScope(Dispatchers.IO).launch {

                            /*
                             * Remove a atividade DO BANCO.
                             */
                            deletarAtividade(atividade, atividadeDAO)

                            /*
                             * Busca de novo as atividades e
                             * atualiza o tempo total da rotina
                             * no banco (dao.atualizar).
                             */
                            val atividadesAtualizadas =
                                buscarAtividadesDaRotina(atividade.idRotina, atividadeDAO)

                            val rotinaTemp = rotina?.copy(
                                tempoMinutosRotina = atividadesAtualizadas.sumOf {
                                    it.duracaoMinutos
                                }
                            )

                            if (rotinaTemp != null) {
                                atualizarRotina(rotinaTemp, rotinaDAO)
                            }

                            atividadesDaRotina = atividadesAtualizadas
                            rotina = rotinaTemp
                        }

                        /*
                         * Remove também o estado
                         * de concluída.
                         */
                        atividadesConcluidas.remove(
                            atividade.id
                        )
                    }
                )
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        /*
         * BOTÃO EDITAR
         */
        BotaoEditar(
            onClick = {

                navController.navigate(
                    "${RotaAbas.TelaCriacao}/editar/${idRotina}"
                )
            }
        )
    }
}


@Composable
fun CardTarefa(
    titulo: String,
    descricao: String,
    isConcluida: Boolean,
    onDeleteClick: () -> Unit = {},
    onCheckClick: () -> Unit = {}
) {

    val verdeCard = Color(0xFF388E3C)

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = verdeCard
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
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


            /*
             * LADO ESQUERDO
             */
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * BOTÃO EXCLUIR
                 */
                Surface(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            onDeleteClick()
                        },

                    shape =
                        RoundedCornerShape(8.dp),

                    color =
                        Color.White.copy(
                            alpha = 0.25f
                        )
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Delete,

                            contentDescription =
                                "Excluir",

                            tint =
                                Color.White,

                            modifier =
                                Modifier.size(20.dp)
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                /*
                 * NOME + DURAÇÃO
                 */
                Column {

                    Text(
                        text = titulo,

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color = Color.White
                    )

                    Text(
                        text = descricao,

                        fontSize = 12.sp,

                        color =
                            Color.White.copy(
                                alpha = 0.9f
                            )
                    )
                }
            }


            /*
             * CHECKBOX
             */
            Icon(
                imageVector =
                    if (isConcluida)
                        Icons.Default.CheckCircle
                    else
                        Icons.Outlined.CheckCircle,

                contentDescription =
                    if (isConcluida)
                        "Concluído"
                    else
                        "Pendente",

                tint = Color.White,

                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onCheckClick()
                    }
            )
        }
    }
}


@Composable
fun BotaoEditar(
    onClick: () -> Unit
) {

    val vermelhoBotao = Color(0xFFFF5252)

    Button(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),

        shape = RoundedCornerShape(16.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = vermelhoBotao
        )
    ) {

        Text(
            text = "Editar",

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            color = Color.White
        )
    }
}