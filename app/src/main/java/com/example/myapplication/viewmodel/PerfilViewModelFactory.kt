package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  PerfilViewModelFactory.kt — pacote ui.perfil║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.PerfilRepository

// Cria o PerfilViewModel já com o PerfilRepository
class PerfilViewModelFactory(
    private val perfilRepository: PerfilRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(perfilRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
