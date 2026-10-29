package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.Atividade
import com.example.myapplication.ui.theme.Rotina

class TelaCriacaoViewModel : ViewModel() {

    // Estados do formulário
    var nomeRotina = androidx.compose.runtime.mutableStateOf("")
        private set

    var inicio = androidx.compose.runtime.mutableStateOf("08:00")
        private set

    var fim = androidx.compose.runtime.mutableStateOf("09:00")
        private set

    var nomeAtividade = androidx.compose.runtime.mutableStateOf("")
        private set

    var duracaoAtividade = androidx.compose.runtime.mutableStateOf("")
        private set


    // Lista reativa de atividades da rotina que está sendo criada.
    // Ela continua em memória porque é só o "rascunho" do formulário:
    // as atividades só vão para o banco quando clicar em "Salvar rotina".
    val atividades = mutableStateListOf<Atividade>()


    // id da rotina que está sendo editada (null = rotina nova)
    var idRotinaEditando = androidx.compose.runtime.mutableStateOf<Int?>(null)
        private set


    // Alterações dos campos
    fun alterarNomeRotina(valor: String) {
        nomeRotina.value = valor
    }

    fun alterarInicio(valor: String) {
        inicio.value = valor
    }

    fun alterarFim(valor: String) {
        fim.value = valor
    }

    fun alterarNomeAtividade(valor: String) {
        nomeAtividade.value = valor
    }

    fun alterarDuracaoAtividade(valor: String) {
        duracaoAtividade.value = valor.filter { it.isDigit() }
    }


    // Adiciona uma atividade à lista do formulário
    fun adicionarAtividade(): Boolean {

        if (
            nomeAtividade.value.isBlank() ||
            duracaoAtividade.value.isBlank()
        ) {
            return false
        }

        val duracao = duracaoAtividade.value.toDoubleOrNull()

        if (duracao == null || duracao <= 0) {
            return false
        }

        // id provisório, só para a LazyColumn do formulário
        // (o id de verdade é gerado pelo banco ao salvar)
        val novoId = if (atividades.isEmpty()) {
            1
        } else {
            atividades.maxOf { it.id } + 1
        }

        val novaAtividade = Atividade(
            id = novoId,
            nomeAtividade = nomeAtividade.value,
            duracaoMinutos = duracao
        )

        atividades.add(novaAtividade)

        // Limpa os campos
        nomeAtividade.value = ""
        duracaoAtividade.value = ""

        return true
    }


    // Remove uma atividade do formulário
    fun removerAtividade(id: Int) {
        atividades.removeAll {
            it.id == id
        }
    }


    // Salva a rotina inteira NO BANCO
    // (chamada dentro de CoroutineScope(Dispatchers.IO).launch na TelaCriacao)
    suspend fun salvarRotina(
        rotinaDAO: RotinaDAO,
        atividadeDAO: AtividadeDAO
    ): Rotina? {

        if (nomeRotina.value.isBlank()) {
            return null
        }

        val tempoTotal = atividades.sumOf {
            it.duracaoMinutos
        }

        val id = idRotinaEditando.value

        // =========================
        // EDITANDO
        // =========================
        if (id != null) {

            val rotina = buscarRotinaPorId(id, rotinaDAO)
                ?: return null

            // mesmo padrão do exemplo de Filmes:
            // copy() com os dados novos + dao.atualizar(...)
            val rotinaTemp = rotina.copy(
                nomeRotina = nomeRotina.value,
                tempoMinutosRotina = tempoTotal,
                horaInicio = inicio.value,
                horaFim = fim.value
            )

            atualizarRotina(rotinaTemp, rotinaDAO)

            // apaga as atividades antigas e grava as do formulário
            deletarAtividadesDaRotina(id, atividadeDAO)

            atividades.forEach { atividade ->

                inserirAtividade(
                    id,
                    atividade.nomeAtividade,
                    atividade.duracaoMinutos,
                    atividadeDAO
                )
            }

            return rotinaTemp
        }


        // =========================
        // CRIANDO NOVA
        // =========================

        val novoId = inserirRotina(
            nomeRotina.value,
            tempoTotal,
            inicio.value,
            fim.value,
            rotinaDAO
        )

        // deu erro ao inserir (o erro já aparece no Log.e)
        if (novoId == -1L) {
            return null
        }

        atividades.forEach { atividade ->

            inserirAtividade(
                novoId.toInt(),
                atividade.nomeAtividade,
                atividade.duracaoMinutos,
                atividadeDAO
            )
        }

        return Rotina(
            id = novoId.toInt(),
            nomeRotina = nomeRotina.value,
            tempoMinutosRotina = tempoTotal,
            horaInicio = inicio.value,
            horaFim = fim.value
        )
    }


    // Busca NO BANCO a rotina que vai ser editada
    // e preenche o formulário com os dados dela
    suspend fun carregarRotinaParaEditar(
        id: Int,
        rotinaDAO: RotinaDAO,
        atividadeDAO: AtividadeDAO
    ) {

        // Já carregou essa rotina? Então não carrega de novo
        // (evita apagar o que o usuário digitou, ex.: ao girar a tela)
        if (idRotinaEditando.value == id) {
            return
        }

        val rotina = buscarRotinaPorId(id, rotinaDAO)
            ?: return

        idRotinaEditando.value = id

        nomeRotina.value = rotina.nomeRotina
        inicio.value = rotina.horaInicio
        fim.value = rotina.horaFim

        atividades.clear()

        atividades.addAll(
            buscarAtividadesDaRotina(id, atividadeDAO)
        )
    }
}
