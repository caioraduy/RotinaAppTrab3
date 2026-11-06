package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  Progresso.kt — pacote ui.progresso         ║
// ╚══════════════════════════════════════════════╝

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodel.ProgressoViewModel


// TELA DE PROGRESSO

@Composable
fun Progresso(
    navController: NavHostController,
    viewModel: ProgressoViewModel
) {

    // O nível e o XP já chegam calculados pelo ProgressoViewModel
    // (antes a tela buscava as atividades no banco e fazia a conta)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EFE0))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Seu progresso",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E1E)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        CardNivel(
            nivel = uiState.nivel,
            xp = uiState.xp,
            xpNoNivel = uiState.xpNoNivel,
            progresso = uiState.progresso
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CardMetricas()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // CardAtividadeGrid()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CardMetasHistorico(navController)

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CardDesempenho(navController)
    }
}

@Composable
fun CardDesempenho(
    navController: NavHostController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(RotaAbas.TelaDesempenho)
            },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF388E3C)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Meu desempenho",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Veja como suas rotinas estão evoluindo.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Abrir desempenho",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


// ============================================================
// CARD NÍVEL
// ============================================================

@Composable
fun CardNivel(
    nivel: Int,
    xp: Int,
    xpNoNivel: Int,
    progresso: Float
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier.size(44.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "🔥",
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Nível $nivel",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$xp XP acumulado",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LinearProgressIndicator(
                progress = { progresso },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),

                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "$xpNoNivel / 100 XP",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = { },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFEBE0)
                    ),

                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Salvar",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Salvar",
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Button(
                    onClick = { },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFEBE0)
                    ),

                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartilhar",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Compartilhar",
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


// ============================================================
// CARD MÉTRICAS
// ============================================================

@Composable
fun CardMetricas() {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE3F2FD),
                modifier = Modifier.size(44.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = "Acompanhe suas métricas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ============================================================
// CARD ATIVIDADE
// ============================================================

@Composable
fun CardAtividadeGrid() {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "ATIVIDADE — ÚLTIMAS 10 SEMANAS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            val tonsVerde = listOf(
                Color(0xFFF0F0F0),
                Color(0xFFC8E6C9),
                Color(0xFF81C784),
                Color(0xFF4CAF50),
                Color(0xFF2E7D32)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                repeat(7) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        repeat(10) {

                            val nivelColor = (0..4).random()

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(
                                        RoundedCornerShape(4.dp)
                                    )
                                    .background(
                                        tonsVerde[nivelColor]
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Menos",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                tonsVerde.forEach { color ->

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(
                                RoundedCornerShape(2.dp)
                            )
                            .background(color)
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )
                }

                Text(
                    text = "Mais",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


// ============================================================
// CARD METAS E HISTÓRICO
// ============================================================

@Composable
fun CardMetasHistorico(
    navController: NavHostController
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Acompanhe sua jornada",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Consulte suas metas e veja seu histórico.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = {
                        navController.navigate(RotaAbas.TelaMetas)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {

                    Text(
                        text = "Metas",
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        navController.navigate(RotaAbas.TelaHistorico)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {

                    Text(
                        text = "Histórico",
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
