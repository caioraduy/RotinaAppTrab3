package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ProgressoViewModel.kt — pacote ui.progresso║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.calcularNivel
import com.example.myapplication.data.local.calcularXp
import com.example.myapplication.data.local.calcularXpNoNivel
import com.example.myapplication.data.repository.AtividadeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressoViewModel(
    private val atividadeRepository: AtividadeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressoUiState())
    val uiState: StateFlow<ProgressoUiState> = _uiState.asStateFlow()

    init {
        // O XP vem das atividades do banco.
        // Se uma atividade é criada ou removida, o nível atualiza sozinho.
        viewModelScope.launch {
            atividadeRepository.getAllAtividades()
                .collect { atividades ->
                    val xpNoNivel = calcularXpNoNivel(atividades)

                    _uiState.update {
                        it.copy(
                            nivel = calcularNivel(atividades),
                            xp = calcularXp(atividades),
                            xpNoNivel = xpNoNivel,
                            progresso = xpNoNivel / 100f
                        )
                    }
                }
        }
    }
}
