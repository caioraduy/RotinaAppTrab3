package com.example.myapplication.data.repository

// ╔══════════════════════════════════════════════╗
// ║  AtividadeRepository.kt — pacote data.repository║
// ║  Única porta de entrada dos dados: só ele    ║
// ║  fala com o AtividadeDAO.                   ║
// ╚══════════════════════════════════════════════╝

import com.example.myapplication.data.local.Atividade
import com.example.myapplication.data.local.AtividadeDAO
import kotlinx.coroutines.flow.Flow

class AtividadeRepository(private val atividadeDao: AtividadeDAO) {

    fun getAllAtividades(): Flow<List<Atividade>> = atividadeDao.buscarTodos()

    fun getAtividadesDaRotina(idRotina: Int): Flow<List<Atividade>> = atividadeDao.buscarPorRotina(idRotina)

    suspend fun inserir(atividade: Atividade) {
        atividadeDao.inserir(atividade)
    }

    suspend fun atualizar(atividade: Atividade) {
        atividadeDao.atualizar(atividade)
    }

    suspend fun deletar(atividade: Atividade) {
        atividadeDao.deletar(atividade)
    }

    // apaga todas as atividades de uma rotina
    suspend fun deletarPorRotina(idRotina: Int) {
        atividadeDao.deletarPorRotina(idRotina)
    }
}
