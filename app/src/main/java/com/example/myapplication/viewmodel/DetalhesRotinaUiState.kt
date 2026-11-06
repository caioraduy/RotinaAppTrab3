package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DetalhesRotinaUiState.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Atividade
import com.example.myapplication.data.local.Rotina



// Tudo o que a TelaRotina (detalhes da rotina) precisa para se desenhar
data class DetalhesRotinaUiState(

    // true enquanto a rotina ainda não chegou do banco ("Carregando...")
    val carregando: Boolean = true,

    // a rotina buscada pelo id (null depois de carregar = "Rotina não encontrada")
    val rotina: Rotina? = null,

    // só as atividades desta rotina
    val atividades: List<Atividade> = emptyList(),

    // ids das atividades marcadas com o ✓
    // (igual ao Trabalho 3: isso fica só na tela, não vai para o banco)
    val atividadesConcluidas: Set<Int> = emptySet()
)
