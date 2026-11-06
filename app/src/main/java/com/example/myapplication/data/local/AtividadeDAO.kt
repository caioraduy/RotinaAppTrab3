package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  AtividadeDAO.kt — pacote data.local        ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AtividadeDAO {
    @Insert
    suspend fun inserir(atividade: Atividade)

    // Trabalho 4: Flow -> a lista atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM atividades")
    fun buscarTodos() : Flow<List<Atividade>>

    @Update
    suspend fun atualizar(atividade: Atividade)

    @Delete
    suspend fun deletar(atividade: Atividade)

    // consulta filtrada: só as atividades de UMA rotina
    @Query("SELECT * FROM atividades WHERE idRotina = :idRotina")
    fun buscarPorRotina(idRotina: Int) : Flow<List<Atividade>>

    // apaga todas as atividades de uma rotina
    // (usado quando a rotina é excluída ou editada)
    @Query("DELETE FROM atividades WHERE idRotina = :idRotina")
    suspend fun deletarPorRotina(idRotina: Int)

}
