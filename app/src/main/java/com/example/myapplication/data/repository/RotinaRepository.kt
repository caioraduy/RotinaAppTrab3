package com.example.myapplication.data.repository

// ╔══════════════════════════════════════════════╗
// ║  RotinaRepository.kt — pacote data.repository║
// ║  Única porta de entrada dos dados: só ele    ║
// ║  fala com o RotinaDAO.                      ║
// ╚══════════════════════════════════════════════╝

import com.example.myapplication.data.local.Rotina
import com.example.myapplication.data.local.RotinaDAO
import kotlinx.coroutines.flow.Flow

class RotinaRepository(private val rotinaDao: RotinaDAO) {

    fun getAllRotinas(): Flow<List<Rotina>> = rotinaDao.buscarTodos()

    fun getRotinaPorId(id: Int): Flow<Rotina?> = rotinaDao.buscarPorId(id)

    // devolve o id que o banco gerou para a rotina nova
    suspend fun inserir(rotina: Rotina): Long {
        return rotinaDao.inserir(rotina)
    }

    suspend fun atualizar(rotina: Rotina) {
        rotinaDao.atualizar(rotina)
    }

    suspend fun deletar(rotina: Rotina) {
        rotinaDao.deletar(rotina)
    }
}
