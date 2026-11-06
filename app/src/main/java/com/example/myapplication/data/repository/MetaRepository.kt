package com.example.myapplication.data.repository

// ╔══════════════════════════════════════════════╗
// ║  MetaRepository.kt — pacote data.repository ║
// ║  Única porta de entrada dos dados: só ele    ║
// ║  fala com o MetaDAO.                        ║
// ╚══════════════════════════════════════════════╝

import com.example.myapplication.data.local.Meta
import com.example.myapplication.data.local.MetaDAO
import kotlinx.coroutines.flow.Flow

class MetaRepository(private val metaDao: MetaDAO) {

    fun getAllMetas(): Flow<List<Meta>> = metaDao.buscarTodos()

    suspend fun inserir(meta: Meta) {
        metaDao.inserir(meta)
    }

    suspend fun atualizar(meta: Meta) {
        metaDao.atualizar(meta)
    }

    suspend fun deletar(meta: Meta) {
        metaDao.deletar(meta)
    }
}
