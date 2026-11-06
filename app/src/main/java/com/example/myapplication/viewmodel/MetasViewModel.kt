package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  MetasViewModel.kt — pacote ui.metas        ║
// ╚══════════════════════════════════════════════╝

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Meta
import com.example.myapplication.data.local.registrarMetaNoHistorico
import com.example.myapplication.data.repository.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MetasViewModel(
    private val metaRepository: MetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetasUiState())
    val uiState: StateFlow<MetasUiState> = _uiState.asStateFlow()

    init {
        // escuta a tabela de metas: a lista e o resumo atualizam sozinhos
        viewModelScope.launch {
            metaRepository.getAllMetas()
                .collect { lista ->
                    _uiState.update {
                        it.copy(
                            listaDeMetas = lista,
                            totalMetas = lista.size,
                            metasConcluidas = lista.count { meta -> meta.concluida }
                        )
                    }
                }
        }
    }


    // =========================
    // DIÁLOGO (adicionar OU atualizar)
    // =========================

    // botão "Nova meta": abre o diálogo limpo (modo adicionar)
    fun onNovaMeta() {
        _uiState.update {
            it.copy(
                nome = "",
                descricao = "",
                metaEmEdicao = null,
                mostrarDialogo = true
            )
        }
    }

    // lápis do card: abre o diálogo com os dados da meta (modo editar)
    fun onEdit(meta: Meta) {
        _uiState.update {
            it.copy(
                nome = meta.nomeMeta,
                descricao = meta.descricaoMeta,
                metaEmEdicao = meta,
                mostrarDialogo = true
            )
        }
    }

    fun onDismissDialogo() {
        _uiState.update {
            it.copy(
                mostrarDialogo = false,
                metaEmEdicao = null
            )
        }
    }

    fun onNomeChange(valor: String) {
        _uiState.update { it.copy(nome = valor) }
    }

    fun onDescricaoChange(valor: String) {
        _uiState.update { it.copy(descricao = valor) }
    }

    // "Adicionar" ou "Atualizar"
    fun onSave() {
        val estado = _uiState.value

        // sem nome não salva (o diálogo continua aberto)
        if (estado.nome.isBlank()) {
            return
        }

        // fecha o diálogo e limpa os campos
        _uiState.update {
            it.copy(
                mostrarDialogo = false,
                metaEmEdicao = null,
                nome = "",
                descricao = ""
            )
        }

        viewModelScope.launch {
            val metaEmEdicao = estado.metaEmEdicao

            if (metaEmEdicao != null) {
                val metaTemp = metaEmEdicao.copy(
                    nomeMeta = estado.nome.trim(),
                    descricaoMeta = estado.descricao.trim()
                )
                metaRepository.atualizar(metaTemp)
            } else {
                metaRepository.inserir(
                    Meta(
                        nomeMeta = estado.nome.trim(),
                        descricaoMeta = estado.descricao.trim()
                    )
                )
            }
        }
    }


    // =========================
    // AÇÕES DO CARD
    // =========================

    // checkbox: concluir / desmarcar
    fun onCheck(meta: Meta) {
        viewModelScope.launch {
            metaRepository.atualizar(meta.copy(concluida = !meta.concluida))
        }

        // só registra no histórico quando CONCLUI
        if (!meta.concluida) {
            registrarMetaNoHistorico(meta.nomeMeta)
        }
    }

    fun onDelete(meta: Meta) {
        viewModelScope.launch {
            metaRepository.deletar(meta)
        }
    }


    // =========================
    // DETALHES DA META
    // =========================

    fun onSelect(meta: Meta) {
        _uiState.update { it.copy(metaSelecionada = meta) }
    }

    fun onDismissDetalhes() {
        _uiState.update { it.copy(metaSelecionada = null) }
    }
}
