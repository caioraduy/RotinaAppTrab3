package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DesempenhoUiState.kt — pacote ui.desempenho║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Rotina



// Tudo o que a TelaDesempenho precisa para se desenhar.
// Os números já chegam calculados pelo DesempenhoViewModel.
data class DesempenhoUiState(

    // card do nível
    val nivel: Int = 1,
    val xpNoNivel: Int = 0,
    val progressoNivel: Float = 0f,

    // cards de estatísticas
    val quantidadeRotinas: Int = 0,
    val quantidadeAtividades: Int = 0,
    val tempoTotal: Double = 0.0,
    val mediaTempoRotina: Double = 0.0,

    // card "Rotina mais longa"
    val maiorRotina: Rotina? = null,

    // lista "Suas rotinas"
    val rotinas: List<Rotina> = emptyList(),

    // id da rotina -> quantidade de atividades dela
    val atividadesPorRotina: Map<Int, Int> = emptyMap(),

    // id da rotina -> parte do tempo total (0 a 1, para a barrinha)
    val percentualPorRotina: Map<Int, Float> = emptyMap()
)
