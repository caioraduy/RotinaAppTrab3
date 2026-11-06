package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  HistoricoUiState.kt — pacote ui.historico  ║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Meta
import com.example.myapplication.data.local.Rotina



// Tudo o que a TelaHistorico precisa para se desenhar
data class HistoricoUiState(

    // listas do banco (chegam pelo Flow do Room)
    val rotinas: List<Rotina> = emptyList(),
    val metas: List<Meta> = emptyList(),

    // números do card "Seu resumo"
    val totalRotinas: Int = 0,
    val totalMetas: Int = 0
)
