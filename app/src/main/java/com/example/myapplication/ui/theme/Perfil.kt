
package com.example.myapplication.ui.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

// Trabalho 2: ficava só no PerfilViewModel (em memória)
// Trabalho 3: virou uma @Entity -> tabela "perfil" no banco Room
@Entity(tableName = "perfil")
data class Perfil (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String = "",
    val email: String = ""

)
