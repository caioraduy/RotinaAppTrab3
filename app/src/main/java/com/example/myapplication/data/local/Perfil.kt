package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  Perfil.kt — pacote data.local              ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Entity
import androidx.room.PrimaryKey

// Trabalho 2: ficava só no PerfilViewModel (em memória)
// Trabalho 3: virou uma @Entity -> tabela "perfil" no banco Room
// Trabalho 4: saiu de ui/theme e veio para o pacote data.local (camada Model)
@Entity(tableName = "perfil")
data class Perfil (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String = "",
    val email: String = ""

)
