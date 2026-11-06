package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  CriacaoRotinaViewModel.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Atividade
import com.example.myapplication.data.local.Rotina
import com.example.myapplication.data.local.registrarRotinaNoHistorico
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.RotinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Antes se chamava TelaCriacaoViewModel e recebia os DAOs nas funções.
// Agora recebe os Repositories no construtor (pela Factory).
class CriacaoRotinaViewModel(
    private val rotinaRepository: RotinaRepository,
    private val atividadeRepository: AtividadeRepository,
    idRotinaParaEditar: Int?   // null = rotina nova
) : ViewModel() {

    private val _uiState = MutableStateFlow(CriacaoRotinaUiState())
    val uiState: StateFlow<CriacaoRotinaUiState> = _uiState.asStateFlow()

    init {
        // Veio um id? Então é edição: preenche o formulário com a rotina do banco.
        // O init roda uma vez só (o ViewModel sobrevive ao girar a tela),
        // então o que o usuário já digitou não é apagado.
        if (idRotinaParaEditar != null) {
            carregarRotinaParaEditar(idRotinaParaEditar)
        }
    }


    // =========================
    // CAMPOS DO FORMULÁRIO
    // =========================

    fun onNomeRotinaChange(valor: String) {
        _uiState.update { it.copy(nomeRotina = valor) }
    }

    fun onInicioChange(valor: String) {
        _uiState.update { it.copy(inicio = valor) }
    }

    fun onFimChange(valor: String) {
        _uiState.update { it.copy(fim = valor) }
    }

    fun onNomeAtividadeChange(valor: String) {
        _uiState.update { it.copy(nomeAtividade = valor) }
    }

    fun onDuracaoAtividadeChange(valor: String) {
        _uiState.update { it.copy(duracaoAtividade = valor.filter { letra -> letra.isDigit() }) }
    }


    // =========================
    // ATIVIDADES (rascunho)
    // =========================

    // Adiciona uma atividade à lista do formulário
    fun onAddAtividade() {
        val estado = _uiState.value
        val duracao = estado.duracaoAtividade.toDoubleOrNull()

        if (estado.nomeAtividade.isBlank() || duracao == null || duracao <= 0) {
            _uiState.update { it.copy(mensagem = "Preencha nome e duração corretamente") }
            return
        }

        // id provisório, só para a LazyColumn do formulário
        // (o id de verdade é gerado pelo banco ao salvar)
        val novoId = if (estado.atividades.isEmpty()) {
            1
        } else {
            estado.atividades.maxOf { it.id } + 1
        }

        val novaAtividade = Atividade(
            id = novoId,
            nomeAtividade = estado.nomeAtividade,
            duracaoMinutos = duracao
        )

        // adiciona e limpa os campos
        _uiState.update {
            it.copy(
                atividades = it.atividades + novaAtividade,
                nomeAtividade = "",
                duracaoAtividade = "",
                mensagem = "Atividade adicionada"
            )
        }
    }

    // Remove uma atividade do formulário
    fun onDeleteAtividade(id: Int) {
        _uiState.update {
            it.copy(
                atividades = it.atividades.filter { atividade -> atividade.id != id },
                mensagem = "Atividade removida"
            )
        }
    }


    // =========================
    // BANCO (pelo Repository)
    // =========================

    // Busca no banco a rotina que vai ser editada
    // e preenche o formulário com os dados dela
    private fun carregarRotinaParaEditar(id: Int) {
        viewModelScope.launch {
            // first() = pega só o valor atual do Flow (uma vez)
            val rotina = rotinaRepository.getRotinaPorId(id).first() ?: return@launch
            val atividades = atividadeRepository.getAtividadesDaRotina(id).first()

            _uiState.update {
                it.copy(
                    idRotinaEditando = id,
                    nomeRotina = rotina.nomeRotina,
                    inicio = rotina.horaInicio,
                    fim = rotina.horaFim,
                    atividades = atividades
                )
            }
        }
    }

    // Salva a rotina inteira no banco (nova ou editada)
    fun onSave() {
        val estado = _uiState.value

        if (estado.nomeRotina.isBlank()) {
            _uiState.update { it.copy(mensagem = "Dê um nome à rotina") }
            return
        }

        // evita salvar duas vezes se clicar rápido no botão
        if (estado.salvando) {
            return
        }

        _uiState.update { it.copy(salvando = true) }

        viewModelScope.launch {
            try {
                val tempoTotal = estado.atividades.sumOf { it.duracaoMinutos }
                val id = estado.idRotinaEditando

                val idDaRotina = if (id != null) {

                    // EDITANDO: mesmo padrão do exemplo de Filmes
                    // (copy() com os dados novos + atualizar)
                    val rotina = rotinaRepository.getRotinaPorId(id).first()
                        ?: throw Exception("Rotina $id não encontrada")

                    val rotinaTemp = rotina.copy(
                        nomeRotina = estado.nomeRotina,
                        tempoMinutosRotina = tempoTotal,
                        horaInicio = estado.inicio,
                        horaFim = estado.fim
                    )

                    rotinaRepository.atualizar(rotinaTemp)

                    // apaga as atividades antigas (as do formulário são gravadas abaixo)
                    atividadeRepository.deletarPorRotina(id)

                    id

                } else {

                    // CRIANDO: o banco gera o id da rotina nova
                    rotinaRepository.inserir(
                        Rotina(
                            nomeRotina = estado.nomeRotina,
                            tempoMinutosRotina = tempoTotal,
                            horaInicio = estado.inicio,
                            horaFim = estado.fim
                        )
                    ).toInt()
                }

                // grava as atividades do formulário ligadas à rotina
                // (id = 0 -> o banco gera o id de cada atividade)
                estado.atividades.forEach { atividade ->
                    atividadeRepository.inserir(
                        Atividade(
                            idRotina = idDaRotina,
                            nomeAtividade = atividade.nomeAtividade,
                            duracaoMinutos = atividade.duracaoMinutos
                        )
                    )
                }

                registrarRotinaNoHistorico(estado.nomeRotina)

                _uiState.update {
                    it.copy(
                        rotinaSalva = true,
                        mensagem = "Rotina salva com ${estado.atividades.size} atividade(s)"
                    )
                }

            } catch (e: Exception) {
                Log.e("Erro ao salvar rotina", "Msg: ${e.message}")

                _uiState.update {
                    it.copy(
                        salvando = false,
                        mensagem = "Erro ao salvar a rotina"
                    )
                }
            }
        }
    }

    // a tela chama depois de mostrar o Toast
    fun onMensagemMostrada() {
        _uiState.update { it.copy(mensagem = null) }
    }
}
