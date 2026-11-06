package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  CriacaoRotinaUiState.kt — pacote ui.rotinas║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Atividade



// Tudo o que a TelaCriacao (nova rotina / editar rotina) precisa para se desenhar
data class CriacaoRotinaUiState(

    // id da rotina que está sendo editada (null = rotina nova)
    val idRotinaEditando: Int? = null,

    // campos do formulário
    val nomeRotina: String = "",
    val inicio: String = "08:00",
    val fim: String = "09:00",
    val nomeAtividade: String = "",
    val duracaoAtividade: String = "",

    // "rascunho" das atividades da rotina.
    // Elas só vão para o banco quando clicar em "Salvar rotina".
    val atividades: List<Atividade> = emptyList(),

    // true enquanto está salvando (o botão fica desabilitado)
    val salvando: Boolean = false,

    // vira true quando terminou de salvar -> a tela volta
    val rotinaSalva: Boolean = false,

    // aviso que a tela mostra num Toast (null = nenhum aviso)
    val mensagem: String? = null
)
