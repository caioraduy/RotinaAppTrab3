package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  PerfilViewModel.kt — pacote ui.perfil      ║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Perfil
import com.example.myapplication.data.repository.PerfilRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val perfilRepository: PerfilRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        // escuta a tabela "perfil" (o app só guarda 1 perfil)
        viewModelScope.launch {
            perfilRepository.getAllPerfis()
                .collect { lista ->
                    val perfil = lista.firstOrNull() ?: Perfil()

                    _uiState.update {
                        it.copy(
                            perfil = perfil,
                            possuiCadastro = perfil.nome.isNotBlank() && perfil.email.isNotBlank()
                        )
                    }
                }
        }
    }

    // chamada antes de abrir a TelaPerfilCont:
    // o formulário começa com os dados que já existem
    fun onEditarPerfil() {
        _uiState.update {
            it.copy(
                nome = it.perfil.nome,
                email = it.perfil.email,
                perfilSalvo = false
            )
        }
    }

    fun onNomeChange(nome: String) {
        _uiState.update { it.copy(nome = nome) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    // Salva ou atualiza o perfil no banco
    fun onSave() {
        val estado = _uiState.value

        if (estado.nome.isBlank() || estado.email.isBlank()) {
            return
        }

        viewModelScope.launch {
            // perfil que está no banco agora (first() = valor atual do Flow)
            val perfilSalvo = perfilRepository.getAllPerfis().first().firstOrNull()

            if (perfilSalvo == null) {
                // ainda não tem cadastro -> insere
                perfilRepository.inserir(
                    Perfil(
                        nome = estado.nome.trim(),
                        email = estado.email.trim()
                    )
                )
            } else {
                // já tem cadastro -> atualiza (copy + atualizar)
                val perfilTemp = perfilSalvo.copy(
                    nome = estado.nome.trim(),
                    email = estado.email.trim()
                )
                perfilRepository.atualizar(perfilTemp)
            }

            _uiState.update { it.copy(perfilSalvo = true) }

        }
    }

    // a TelaPerfilCont chama quando já voltou para o perfil
    fun onPerfilSalvoMostrado() {
        _uiState.update { it.copy(perfilSalvo = false) }
    }
}
