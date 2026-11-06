package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  ListaRotinasViewModelFactory.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository

// O Android não sabe criar sozinho um ViewModel que recebe parâmetros.
// A Factory faz isso: cria o ListaRotinasViewModel já com os Repositories.
class ListaRotinasViewModelFactory(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaRotinasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListaRotinasViewModel(rotinaRepository, atividadeRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
