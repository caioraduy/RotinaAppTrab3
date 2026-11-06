package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  MetasViewModelFactory.kt — pacote ui.metas ║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.MetaRepository

// Cria o MetasViewModel já com o MetaRepository
class MetasViewModelFactory(
    private val metaRepository: MetaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MetasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MetasViewModel(metaRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
