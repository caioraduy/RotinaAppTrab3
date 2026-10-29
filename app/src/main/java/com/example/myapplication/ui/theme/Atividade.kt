
package com.example.myapplication.ui.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

// Trabalho 2: data class + lista global em memória (mutableStateListOf)
// Trabalho 3: virou uma @Entity -> tabela "atividades" no banco Room
@Entity(tableName = "atividades")
data class Atividade (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // id da rotina dona desta atividade
    val idRotina: Int = 0,

    val nomeAtividade: String = "",
    val duracaoMinutos: Double = 0.0

)

// O XP agora é calculado com a lista de atividades
// que veio do banco (antes usava a lista global em memória)
fun calcularXp(atividades: List<Atividade>): Int {
    return atividades.sumOf {
        it.duracaoMinutos.toInt()
    }
}

fun calcularNivel(atividades: List<Atividade>): Int {
    val xp = calcularXp(atividades)
    return (xp / 100) + 1
}

fun calcularXpNoNivel(atividades: List<Atividade>): Int {
    val xp = calcularXp(atividades)
    return xp % 100
}
