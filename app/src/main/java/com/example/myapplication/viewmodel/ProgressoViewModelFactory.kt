package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ProgressoViewModelFactory.kt — pacote ui.progresso║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.AtividadeRepository

// Cria o ProgressoViewModel já com o AtividadeRepository
class ProgressoViewModelFactory(
    private val atividadeRepository: AtividadeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgressoViewModel(atividadeRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
