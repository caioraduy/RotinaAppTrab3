
package com.example.myapplication.ui.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

// Trabalho 2: data class + lista global em memória (mutableStateListOf)
// Trabalho 3: virou uma @Entity -> tabela "rotinas" no banco Room
@Entity(tableName = "rotinas")
data class Rotina (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nomeRotina: String,
    val tempoMinutosRotina: Double,

    // campos "Início" e "Fim" da TelaCriacao
    // (no Trabalho 2 eles não eram salvos)
    val horaInicio: String = "08:00",
    val horaFim: String = "09:00"

)
