package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  HistoricoViewModelFactory.kt — pacote ui.historico║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.MetaRepository
import com.example.myapplication.data.repository.RotinaRepository

// Cria o HistoricoViewModel já com os Repositories
class HistoricoViewModelFactory(
    private val rotinaRepository: RotinaRepository,
    private val metaRepository: MetaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoricoViewModel(rotinaRepository, metaRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
