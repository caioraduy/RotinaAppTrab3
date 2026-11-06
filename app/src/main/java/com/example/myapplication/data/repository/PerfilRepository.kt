package com.example.myapplication.data.repository

// ╔══════════════════════════════════════════════╗
// ║  PerfilRepository.kt — pacote data.repository║
// ║  Única porta de entrada dos dados: só ele    ║
// ║  fala com o PerfilDAO.                      ║
// ╚══════════════════════════════════════════════╝

import com.example.myapplication.data.local.Perfil
import com.example.myapplication.data.local.PerfilDAO
import kotlinx.coroutines.flow.Flow

class PerfilRepository(private val perfilDao: PerfilDAO) {

    fun getAllPerfis(): Flow<List<Perfil>> = perfilDao.buscarTodos()

    suspend fun inserir(perfil: Perfil) {
        perfilDao.inserir(perfil)
    }

    suspend fun atualizar(perfil: Perfil) {
        perfilDao.atualizar(perfil)
    }

    suspend fun deletar(perfil: Perfil) {
        perfilDao.deletar(perfil)
    }
}
