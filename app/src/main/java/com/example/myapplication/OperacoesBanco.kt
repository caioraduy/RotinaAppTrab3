package com.example.myapplication

import android.util.Log
import com.example.myapplication.ui.theme.Atividade
import com.example.myapplication.ui.theme.Meta
import com.example.myapplication.ui.theme.Perfil
import com.example.myapplication.ui.theme.Rotina

// ============================================================
// ROTINAS
// ============================================================

// devolve o id da rotina criada (ou -1 se der erro)
suspend fun inserirRotina(nome: String, tempo: Double, inicio: String, fim: String, rotinaDao: RotinaDAO): Long {
    try {
        return rotinaDao.inserir(Rotina(0, nome, tempo, inicio, fim))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar rotina", "Msg: ${e.message}")
        return -1L
    }
}

suspend fun buscarRotinas(rotinaDao: RotinaDAO): List<Rotina> {
    try {
        return rotinaDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar rotinas", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun buscarRotinaPorId(id: Int, rotinaDao: RotinaDAO): Rotina? {
    try {
        return rotinaDao.buscarPorId(id)
    } catch (e: Exception) {
        Log.e("Erro ao buscar rotina", "Msg: ${e.message}")
        return null
    }
}

suspend fun deletarRotina(rotina: Rotina, rotinaDao: RotinaDAO){
    try {
        rotinaDao.deletar(rotina)
    }catch (e: Exception){
        Log.e("Erro ao deletar rotina", "Msg ${e.message}")
    }
}

suspend fun atualizarRotina(rotina: Rotina, rotinaDao: RotinaDAO){
    try {
        rotinaDao.atualizar(rotina)
    }catch (e: Exception){
        Log.e("Erro ao atualizar rotina", "Msg ${e.message}")
    }
}


// ============================================================
// ATIVIDADES
// ============================================================

suspend fun inserirAtividade(idRotina: Int, nome: String, duracao: Double, atividadeDao: AtividadeDAO) {
    try {
        atividadeDao.inserir(Atividade(0, idRotina, nome, duracao))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar atividade", "Msg: ${e.message}")
    }
}

suspend fun buscarAtividades(atividadeDao: AtividadeDAO): List<Atividade> {
    try {
        return atividadeDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar atividades", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun buscarAtividadesDaRotina(idRotina: Int, atividadeDao: AtividadeDAO): List<Atividade> {
    try {
        return atividadeDao.buscarPorRotina(idRotina)
    } catch (e: Exception) {
        Log.e("Erro ao buscar atividades", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarAtividade(atividade: Atividade, atividadeDao: AtividadeDAO){
    try {
        atividadeDao.deletar(atividade)
    }catch (e: Exception){
        Log.e("Erro ao deletar atividade", "Msg ${e.message}")
    }
}

suspend fun deletarAtividadesDaRotina(idRotina: Int, atividadeDao: AtividadeDAO){
    try {
        atividadeDao.deletarPorRotina(idRotina)
    }catch (e: Exception){
        Log.e("Erro ao deletar atividades", "Msg ${e.message}")
    }
}

suspend fun atualizarAtividade(atividade: Atividade, atividadeDao: AtividadeDAO){
    try {
        atividadeDao.atualizar(atividade)
    }catch (e: Exception){
        Log.e("Erro ao atualizar atividade", "Msg ${e.message}")
    }
}


// ============================================================
// METAS
// ============================================================

suspend fun inserirMeta(nome: String, descricao: String, metaDao: MetaDAO) {
    try {
        metaDao.inserir(Meta(0, nome, descricao))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar meta", "Msg: ${e.message}")
    }
}

suspend fun buscarMetas(metaDao: MetaDAO): List<Meta> {
    try {
        return metaDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar metas", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarMeta(meta: Meta, metaDao: MetaDAO){
    try {
        metaDao.deletar(meta)
    }catch (e: Exception){
        Log.e("Erro ao deletar meta", "Msg ${e.message}")
    }
}

suspend fun atualizarMeta(meta: Meta, metaDao: MetaDAO){
    try {
        metaDao.atualizar(meta)
    }catch (e: Exception){
        Log.e("Erro ao atualizar meta", "Msg ${e.message}")
    }
}


// ============================================================
// PERFIL
// ============================================================

suspend fun inserirPerfil(nome: String, email: String, perfilDao: PerfilDAO) {
    try {
        perfilDao.inserir(Perfil(0, nome, email))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar perfil", "Msg: ${e.message}")
    }
}

suspend fun buscarPerfis(perfilDao: PerfilDAO): List<Perfil> {
    try {
        return perfilDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar perfil", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarPerfil(perfil: Perfil, perfilDao: PerfilDAO){
    try {
        perfilDao.deletar(perfil)
    }catch (e: Exception){
        Log.e("Erro ao deletar perfil", "Msg ${e.message}")
    }
}

suspend fun atualizarPerfil(perfil: Perfil, perfilDao: PerfilDAO){
    try {
        perfilDao.atualizar(perfil)
    }catch (e: Exception){
        Log.e("Erro ao atualizar perfil", "Msg ${e.message}")
    }
}
