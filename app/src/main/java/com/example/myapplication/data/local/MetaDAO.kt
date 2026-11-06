package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  MetaDAO.kt — pacote data.local             ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDAO {
    @Insert
    suspend fun inserir(meta: Meta)

    // Trabalho 4: Flow -> a lista atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM metas")
    fun buscarTodos() : Flow<List<Meta>>

    @Update
    suspend fun atualizar(meta: Meta)

    @Delete
    suspend fun deletar(meta: Meta)

}
