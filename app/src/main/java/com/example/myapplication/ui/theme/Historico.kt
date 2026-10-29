
package com.example.myapplication.ui.theme

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Historico(
    val idHistorico: Int,
    val data: String,
    val rotinaConcluida: Boolean = false,
    val nomeRotina: String = "",
    val metaConcluida: Boolean = false,
    val nomeMeta: String = ""
)

// ============================================================
// LISTA GLOBAL E REATIVA DO HISTÓRICO
// ============================================================

val listaHistorico = mutableStateListOf<Historico>()

// ============================================================
// PRÓXIMO ID
// ============================================================

fun proximoIdHistorico(): Int {
    return if (listaHistorico.isEmpty()) {
        1
    } else {
        listaHistorico.maxOf { it.idHistorico } + 1
    }
}

// ============================================================
// DATA ATUAL
// ============================================================

fun dataAtual(): String {
    val formato = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    )

    return formato.format(Date())
}

// ============================================================
// REGISTRAR ROTINA CONCLUÍDA
// ============================================================

fun registrarRotinaNoHistorico(nomeRotina: String) {

    listaHistorico.add(
        Historico(
            idHistorico = proximoIdHistorico(),
            data = dataAtual(),
            rotinaConcluida = true,
            nomeRotina = nomeRotina
        )
    )
}

// ============================================================
// REGISTRAR META CONCLUÍDA
// ============================================================

fun registrarMetaNoHistorico(nomeMeta: String) {

    listaHistorico.add(
        Historico(
            idHistorico = proximoIdHistorico(),
            data = dataAtual(),
            metaConcluida = true,
            nomeMeta = nomeMeta
        )
    )
}

// ============================================================
// REMOVER HISTÓRICO
// ============================================================

fun removerDoHistorico(historico: Historico) {
    listaHistorico.removeAll {
        it.idHistorico == historico.idHistorico
    }
}
