package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ui.theme.Meta

@Dao
interface MetaDAO {
    @Insert
    suspend fun inserir(meta: Meta)

    @Query("SELECT * FROM metas")
    suspend fun buscarTodos() : List<Meta>

    @Update
    suspend fun atualizar(meta: Meta)

    @Delete
    suspend fun deletar(meta: Meta)

}
