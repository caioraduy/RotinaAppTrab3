package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ListaRotinasUiState.kt — pacote ui.rotinas ║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Rotina



// Tudo o que a TelaListaRotinas precisa para se desenhar
data class ListaRotinasUiState(

    // rotinas salvas no banco (chegam pelo Flow do Room)
    val listaDeRotinas: List<Rotina> = emptyList(),

    // rotina em que o usuário clicou no X.
    // Enquanto não for null, aparece o diálogo "Excluir rotina?"
    val rotinaParaExcluir: Rotina? = null
)
