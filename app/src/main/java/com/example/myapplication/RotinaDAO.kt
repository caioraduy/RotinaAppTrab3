package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ui.theme.Rotina

@Dao
interface RotinaDAO {
    // devolve o id gerado pelo banco
    // (usado para ligar as atividades à rotina nova)
    @Insert
    suspend fun inserir(rotina: Rotina) : Long

    @Query("SELECT * FROM rotinas")
    suspend fun buscarTodos() : List<Rotina>

    @Update
    suspend fun atualizar(rotina: Rotina)

    @Delete
    suspend fun deletar(rotina: Rotina)

    // consulta filtrada: busca UMA rotina pelo id
    // (usada na tela de Detalhes e na edição)
    @Query("SELECT * FROM rotinas WHERE id = :id")
    suspend fun buscarPorId(id: Int) : Rotina?

}
