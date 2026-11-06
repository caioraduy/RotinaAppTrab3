package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DetalhesRotinaViewModel.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Atividade
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalhesRotinaViewModel(
    idRotina: Int?,
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalhesRotinaUiState())
    val uiState: StateFlow<DetalhesRotinaUiState> = _uiState.asStateFlow()

    init {
        if (idRotina == null) {
            // sem id não tem o que buscar -> "Rotina não encontrada"
            _uiState.update { it.copy(carregando = false) }
        } else {
            // A tela recebe SÓ O ID pela navegação.
            // Aqui o ViewModel escuta a rotina (pelo id) e as atividades dela.
            // Se a rotina for editada ou uma atividade for removida,
            // o Room avisa e a tela atualiza sozinha.
            viewModelScope.launch {
                rotinaRepository.getRotinaPorId(idRotina)
                    .collect { rotina ->
                        _uiState.update { it.copy(rotina = rotina, carregando = false) }
                    }
            }

            viewModelScope.launch {
                atividadeRepository.getAtividadesDaRotina(idRotina)
                    .collect { lista ->
                        _uiState.update { it.copy(atividades = lista) }
                    }
            }
        }
    }

    // ✓ da atividade: marca / desmarca (só na tela)
    fun onCheck(idAtividade: Int) {
        _uiState.update {
            val concluidas = if (idAtividade in it.atividadesConcluidas) {
                it.atividadesConcluidas - idAtividade
            } else {
                it.atividadesConcluidas + idAtividade
            }
            it.copy(atividadesConcluidas = concluidas)
        }
    }

    // Remove a atividade do banco e atualiza o tempo total da rotina.
    // A lista e o tempo na tela mudam sozinhos (Flow).
    fun onDelete(atividade: Atividade) {
        _uiState.update {
            it.copy(atividadesConcluidas = it.atividadesConcluidas - atividade.id)
        }

        viewModelScope.launch {
            atividadeRepository.deletar(atividade)

            // recalcula o tempo total com as atividades que sobraram
            val atividadesRestantes =
                atividadeRepository.getAtividadesDaRotina(atividade.idRotina).first()

            val rotina = rotinaRepository.getRotinaPorId(atividade.idRotina).first()

            if (rotina != null) {
                val rotinaTemp = rotina.copy(
                    tempoMinutosRotina = atividadesRestantes.sumOf { it.duracaoMinutos }
                )
                rotinaRepository.atualizar(rotinaTemp)
            }
        }
    }
}
