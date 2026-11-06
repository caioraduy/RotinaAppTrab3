package com.example.myapplication.ui

// ╔══════════════════════════════════════════════╗
// ║  AppNavegacao.kt — pacote ui                ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.MetaRepository
import com.example.myapplication.data.repository.PerfilRepository
import com.example.myapplication.data.repository.RotinaRepository
import com.example.myapplication.viewmodel.CriacaoRotinaViewModelFactory
import com.example.myapplication.viewmodel.DesempenhoViewModelFactory
import com.example.myapplication.viewmodel.DetalhesRotinaViewModelFactory
import com.example.myapplication.viewmodel.HistoricoViewModelFactory
import com.example.myapplication.viewmodel.ListaRotinasViewModelFactory
import com.example.myapplication.viewmodel.MetasViewModelFactory
import com.example.myapplication.viewmodel.PerfilViewModel
import com.example.myapplication.viewmodel.PerfilViewModelFactory
import com.example.myapplication.viewmodel.ProgressoViewModelFactory


// ============================================================
// NAVEGAÇÃO PRINCIPAL
// ============================================================
// Aqui cada tela recebe o SEU ViewModel, criado pela Factory
// com o Repository certo: viewModel(factory = ...).
// As telas não conhecem banco, DAO nem Repository: só o ViewModel.

@Composable
fun AppNavegacao(
    rotinaRepository: RotinaRepository,
    atividadeRepository: AtividadeRepository,
    metaRepository: MetaRepository,
    perfilRepository: PerfilRepository
) {

    val navController = rememberNavController()

    // UM ÚNICO ViewModel para as duas telas de perfil
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(perfilRepository)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF3EFE0),

        bottomBar = {
            BarraNavegacao(navController)
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = RotaAbas.TelaListaRotina,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // LISTA DE ROTINAS
            composable(RotaAbas.TelaListaRotina) {
                TelaListaRotinas(
                    navController = navController,
                    viewModel = viewModel(
                        factory = ListaRotinasViewModelFactory(rotinaRepository, atividadeRepository)
                    )
                )
            }

            // PROGRESSO
            composable(RotaAbas.Progresso) {
                Progresso(
                    navController = navController,
                    viewModel = viewModel(
                        factory = ProgressoViewModelFactory(atividadeRepository)
                    )
                )
            }

            // PERFIL
            composable(RotaAbas.TelaPerfil) {
                TelaPerfil(
                    navController = navController,
                    viewModel = perfilViewModel
                )
            }

            // EDITAR / CADASTRAR PERFIL
            composable(RotaAbas.TelaPerfilCont) {
                TelaPerfilCont(
                    navController = navController,
                    viewModel = perfilViewModel
                )
            }

            // NOVA ROTINA
            composable(RotaAbas.TelaCriacao) {
                TelaCriacao(
                    navController = navController,
                    viewModel = viewModel(
                        factory = CriacaoRotinaViewModelFactory(rotinaRepository, atividadeRepository)
                    )
                )
            }

            // EDITAR ROTINA (o ViewModel recebe o id e busca a rotina no banco)
            composable(
                route = "${RotaAbas.TelaCriacao}/editar/{idRotina}"
            ) { backStackEntry ->

                val id = backStackEntry
                    .arguments
                    ?.getString("idRotina")
                    ?.toIntOrNull()

                TelaCriacao(
                    navController = navController,
                    viewModel = viewModel(
                        factory = CriacaoRotinaViewModelFactory(rotinaRepository, atividadeRepository, idRotinaParaEditar = id)
                    )
                )
            }

            // DETALHES DA ROTINA (recebe só o id)
            composable(
                route = "${RotaAbas.TelaRotina}/{idRotina}"
            ) { backStackEntry ->

                val idRotina = backStackEntry
                    .arguments
                    ?.getString("idRotina")
                    ?.toIntOrNull()

                TelaRotina(
                    navController = navController,
                    idRotina = idRotina,
                    viewModel = viewModel(
                        factory = DetalhesRotinaViewModelFactory(idRotina, rotinaRepository, atividadeRepository)
                    )
                )
            }

            // HISTÓRICO
            composable(RotaAbas.TelaHistorico) {
                TelaHistorico(
                    navController = navController,
                    viewModel = viewModel(
                        factory = HistoricoViewModelFactory(rotinaRepository, metaRepository)
                    )
                )
            }

            // METAS
            composable(RotaAbas.TelaMetas) {
                TelaMetas(
                    navController = navController,
                    viewModel = viewModel(
                        factory = MetasViewModelFactory(metaRepository)
                    )
                )
            }

            // DESEMPENHO
            composable(RotaAbas.TelaDesempenho) {
                TelaDesempenho(
                    navController = navController,
                    viewModel = viewModel(
                        factory = DesempenhoViewModelFactory(rotinaRepository, atividadeRepository)
                    )
                )
            }
        }
    }
}


// ============================================================
// BARRA DE NAVEGAÇÃO
// ============================================================

@Composable
fun BarraNavegacao(
    navInterno: NavHostController
) {

    val rotaAtual =
        navInterno.currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFFF3EFE0)
    ) {

        NavigationBarItem(
            selected = rotaAtual == RotaAbas.TelaListaRotina,

            onClick = {
                navInterno.navigate(RotaAbas.TelaListaRotina) {
                    popUpTo(RotaAbas.TelaListaRotina) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Rotinas"
                )
            },

            label = {
                Text("Rotinas")
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = rotaAtual == RotaAbas.Progresso,

            onClick = {
                navInterno.navigate(RotaAbas.Progresso) {
                    popUpTo(RotaAbas.TelaListaRotina) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Progresso"
                )
            },

            label = {
                Text("Progresso")
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = rotaAtual == RotaAbas.TelaPerfil,

            onClick = {
                navInterno.navigate(RotaAbas.TelaPerfil) {
                    popUpTo(RotaAbas.TelaListaRotina) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil"
                )
            },

            label = {
                Text("Perfil")
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4CAF50),
                selectedTextColor = Color(0xFF4CAF50),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
