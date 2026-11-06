package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DesempenhoViewModelFactory.kt — pacote ui.desempenho║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository

// Cria o DesempenhoViewModel já com os Repositories
class DesempenhoViewModelFactory(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DesempenhoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DesempenhoViewModel(rotinaRepository, atividadeRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
