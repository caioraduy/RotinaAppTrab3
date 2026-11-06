package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  RotinaDAO.kt — pacote data.local           ║
// ╚══════════════════════════════════════════════╝

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RotinaDAO {
    // devolve o id gerado pelo banco
    // (usado para ligar as atividades à rotina nova)
    @Insert
    suspend fun inserir(rotina: Rotina) : Long

    // Trabalho 4: devolve um Flow (e não é mais suspend).
    // O Room manda a lista nova sozinho sempre que a tabela "rotinas" muda.
    @Query("SELECT * FROM rotinas")
    fun buscarTodos() : Flow<List<Rotina>>

    @Update
    suspend fun atualizar(rotina: Rotina)

    @Delete
    suspend fun deletar(rotina: Rotina)

    // consulta filtrada: busca UMA rotina pelo id
    // (usada na tela de Detalhes e na edição)
    // Flow também: se a rotina for editada, a tela de Detalhes atualiza sozinha
    @Query("SELECT * FROM rotinas WHERE id = :id")
    fun buscarPorId(id: Int) : Flow<Rotina?>

}
