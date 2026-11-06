package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ListaRotinasViewModel.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Rotina
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaRotinasViewModel(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModel() {

    // privado: só o ViewModel altera
    private val _uiState = MutableStateFlow(ListaRotinasUiState())

    // público e só leitura: a tela observa
    val uiState: StateFlow<ListaRotinasUiState> = _uiState.asStateFlow()

    init {
        // Fica "escutando" a tabela de rotinas.
        // Sempre que uma rotina é inserida, editada ou excluída,
        // o Room manda a lista nova e o _uiState é atualizado sozinho.
        viewModelScope.launch {
            rotinaRepository.getAllRotinas()
                .collect { lista ->
                    Log.d("Busca Ok", "... $lista")
                    _uiState.update { it.copy(listaDeRotinas = lista) }
                }
        }
    }

    // clicou no X do card: abre o diálogo de confirmação
    fun onDelete(rotina: Rotina) {
        _uiState.update { it.copy(rotinaParaExcluir = rotina) }
    }

    // clicou em "Cancelar" ou fora do diálogo
    fun onCancelDelete() {
        _uiState.update { it.copy(rotinaParaExcluir = null) }
    }

    // clicou em "Excluir": remove a rotina e as atividades dela.
    // Não precisa buscar a lista de novo: o Flow já atualiza a tela.
    fun onConfirmDelete() {
        val rotina = _uiState.value.rotinaParaExcluir ?: return

        _uiState.update { it.copy(rotinaParaExcluir = null) }

        viewModelScope.launch {
            atividadeRepository.deletarPorRotina(rotina.id)
            rotinaRepository.deletar(rotina)
        }
    }
}
