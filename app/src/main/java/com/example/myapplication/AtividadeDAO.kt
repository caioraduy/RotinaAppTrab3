package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ui.theme.Atividade

@Dao
interface AtividadeDAO {
    @Insert
    suspend fun inserir(atividade: Atividade)

    @Query("SELECT * FROM atividades")
    suspend fun buscarTodos() : List<Atividade>

    @Update
    suspend fun atualizar(atividade: Atividade)

    @Delete
    suspend fun deletar(atividade: Atividade)

    // consulta filtrada: só as atividades de UMA rotina
    @Query("SELECT * FROM atividades WHERE idRotina = :idRotina")
    suspend fun buscarPorRotina(idRotina: Int) : List<Atividade>

    // apaga todas as atividades de uma rotina
    // (usado quando a rotina é excluída ou editada)
    @Query("DELETE FROM atividades WHERE idRotina = :idRotina")
    suspend fun deletarPorRotina(idRotina: Int)

}
