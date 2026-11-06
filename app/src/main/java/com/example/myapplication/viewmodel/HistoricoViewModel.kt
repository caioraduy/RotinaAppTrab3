package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  HistoricoViewModel.kt — pacote ui.historico║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.MetaRepository
import com.example.myapplication.data.repository.RotinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoricoViewModel(
    private val rotinaRepository: RotinaRepository,
    private val metaRepository: MetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricoUiState())
    val uiState: StateFlow<HistoricoUiState> = _uiState.asStateFlow()

    init {
        // escuta as rotinas
        viewModelScope.launch {
            rotinaRepository.getAllRotinas()
                .collect { lista ->
                    _uiState.update { it.copy(rotinas = lista, totalRotinas = lista.size) }
                }
        }

        // escuta as metas
        viewModelScope.launch {
            metaRepository.getAllMetas()
                .collect { lista ->
                    _uiState.update { it.copy(metas = lista, totalMetas = lista.size) }
                }
        }
    }
}
