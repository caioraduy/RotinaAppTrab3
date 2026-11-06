package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  Meta.kt — pacote data.local                ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Entity
import androidx.room.PrimaryKey

// Trabalho 2: data class + lista global em memória (mutableStateListOf)
// Trabalho 3: virou uma @Entity -> tabela "metas" no banco Room
// Trabalho 4: saiu de ui/theme e veio para o pacote data.local (camada Model)
@Entity(tableName = "metas")
data class Meta (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nomeMeta: String,
    val descricaoMeta: String,
    val concluida: Boolean = false

)
