package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  CriacaoRotinaViewModelFactory.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository

// Cria o CriacaoRotinaViewModel já com os Repositories.
// idRotinaParaEditar: null = nova rotina / um número = editar essa rotina
class CriacaoRotinaViewModelFactory(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository,
    private val idRotinaParaEditar: Int? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriacaoRotinaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CriacaoRotinaViewModel(
                rotinaRepository,
                atividadeRepository,
                idRotinaParaEditar
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
