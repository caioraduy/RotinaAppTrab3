package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ProgressoUiState.kt — pacote ui.progresso  ║
// ╚══════════════════════════════════════════════╝


// Tudo o que a tela Progresso precisa para se desenhar (card do nível)
data class ProgressoUiState(
    val nivel: Int = 1,
    val xp: Int = 0,
    val xpNoNivel: Int = 0,

    // 0 a 1, para a barra de progresso
    val progresso: Float = 0f
)
