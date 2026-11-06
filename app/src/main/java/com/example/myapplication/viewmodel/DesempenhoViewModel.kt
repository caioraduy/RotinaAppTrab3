package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DesempenhoViewModel.kt — pacote ui.desempenho║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Atividade
import com.example.myapplication.data.local.Rotina
import com.example.myapplication.data.local.calcularNivel
import com.example.myapplication.data.local.calcularXpNoNivel
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DesempenhoViewModel(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DesempenhoUiState())
    val uiState: StateFlow<DesempenhoUiState> = _uiState.asStateFlow()

    init {
        // combine = junta os dois Flows (rotinas + atividades).
        // Sempre que um dos dois muda no banco, as contas são refeitas.
        viewModelScope.launch {
            combine(
                rotinaRepository.getAllRotinas(),
                atividadeRepository.getAllAtividades()
            ) { rotinas, atividades ->
                calcularDesempenho(rotinas, atividades)
            }
                .collect { novoEstado ->
                    _uiState.value = novoEstado
                }
        }
    }

    // as mesmas contas que o DesempenhoViewModel já fazia no Trabalho 3,
    // agora juntas num único UiState
    private fun calcularDesempenho(
        rotinas: List<Rotina>,
        atividades: List<Atividade>
    ): DesempenhoUiState {

        val tempoTotal = rotinas.sumOf { it.tempoMinutosRotina }

        val media = if (rotinas.isEmpty()) {
            0.0
        } else {
            tempoTotal / rotinas.size
        }

        val xpNoNivel = calcularXpNoNivel(atividades)

        return DesempenhoUiState(
            nivel = calcularNivel(atividades),
            xpNoNivel = xpNoNivel,
            progressoNivel = xpNoNivel / 100f,
            quantidadeRotinas = rotinas.size,
            quantidadeAtividades = atividades.size,
            tempoTotal = tempoTotal,
            mediaTempoRotina = media,
            maiorRotina = rotinas.maxByOrNull { it.tempoMinutosRotina },
            rotinas = rotinas,
            atividadesPorRotina = rotinas.associate { rotina ->
                rotina.id to atividades.count { it.idRotina == rotina.id }
            },
            percentualPorRotina = rotinas.associate { rotina ->
                val percentual = if (tempoTotal <= 0) {
                    0f
                } else {
                    (rotina.tempoMinutosRotina / tempoTotal).toFloat()
                }
                rotina.id to percentual
            }
        )
    }
}
