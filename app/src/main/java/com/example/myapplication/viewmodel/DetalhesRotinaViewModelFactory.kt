package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  DetalhesRotinaViewModelFactory.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository

// Cria o DetalhesRotinaViewModel com o id da rotina e os Repositories
class DetalhesRotinaViewModelFactory(
    private val idRotina: Int?,
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalhesRotinaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalhesRotinaViewModel(
                idRotina,
                rotinaRepository,
                atividadeRepository
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
