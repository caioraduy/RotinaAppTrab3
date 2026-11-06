package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  PerfilDAO.kt — pacote data.local           ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDAO {
    @Insert
    suspend fun inserir(perfil: Perfil)

    // Trabalho 4: Flow -> o perfil atualiza sozinho quando a tabela muda
    @Query("SELECT * FROM perfil")
    fun buscarTodos() : Flow<List<Perfil>>

    @Update
    suspend fun atualizar(perfil: Perfil)

    @Delete
    suspend fun deletar(perfil: Perfil)

}
