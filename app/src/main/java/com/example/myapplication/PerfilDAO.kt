package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.ui.theme.Perfil

@Dao
interface PerfilDAO {
    @Insert
    suspend fun inserir(perfil: Perfil)

    @Query("SELECT * FROM perfil")
    suspend fun buscarTodos() : List<Perfil>

    @Update
    suspend fun atualizar(perfil: Perfil)

    @Delete
    suspend fun deletar(perfil: Perfil)

}
