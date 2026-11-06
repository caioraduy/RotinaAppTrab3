package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  MetasUiState.kt — pacote ui.metas          ║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Meta



// Tudo o que a TelaMetas precisa para se desenhar
data class MetasUiState(

    // metas salvas no banco (chegam pelo Flow do Room)
    val listaDeMetas: List<Meta> = emptyList(),

    // resumo do topo: "X de Y metas concluídas"
    val totalMetas: Int = 0,
    val metasConcluidas: Int = 0,

    // diálogo de adicionar / editar
    // (metaEmEdicao = null -> nova meta / preenchida -> editando)
    val mostrarDialogo: Boolean = false,
    val metaEmEdicao: Meta? = null,

    // campos do diálogo
    val nome: String = "",
    val descricao: String = "",

    // meta clicada (abre o diálogo de detalhes)
    val metaSelecionada: Meta? = null
) {
    // Propriedades calculadas (igual ao textoBotao do exemplo de Filmes):
    // em vez de um "estouEditando" separado, derivamos os textos do metaEmEdicao
    val tituloDialogo: String
        get() = if (metaEmEdicao == null) "Nova meta" else "Editar meta"

    val textoBotao: String
        get() = if (metaEmEdicao == null) "Adicionar" else "Atualizar"
}
