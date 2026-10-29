
package com.example.myapplication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.Perfil

class PerfilViewModel : ViewModel() {

    // Dados do perfil
    var perfil = mutableStateOf(Perfil())
        private set

    // Verifica se o usuário já possui cadastro
    fun possuiCadastro(): Boolean {
        return perfil.value.nome.isNotBlank() &&
                perfil.value.email.isNotBlank()
    }

    // Busca o perfil salvo NO BANCO (o app só guarda 1 perfil)
    // (chamada dentro de CoroutineScope(Dispatchers.IO).launch na TelaPerfil)
    suspend fun carregarPerfil(perfilDAO: PerfilDAO) {
        perfil.value = buscarPerfis(perfilDAO).firstOrNull() ?: Perfil()
    }

    // Salvar ou atualizar perfil NO BANCO
    suspend fun salvarPerfil(
        nome: String,
        email: String,
        perfilDAO: PerfilDAO
    ) {

        val perfilSalvo = buscarPerfis(perfilDAO).firstOrNull()

        if (perfilSalvo == null) {
            // ainda não tem cadastro -> insere
            inserirPerfil(nome.trim(), email.trim(), perfilDAO)
        } else {
            // já tem cadastro -> atualiza (copy + dao.atualizar)
            val perfilTemp = perfilSalvo.copy(
                nome = nome.trim(),
                email = email.trim()
            )
            atualizarPerfil(perfilTemp, perfilDAO)
        }

        carregarPerfil(perfilDAO)
    }

    // Alterar somente o nome
    fun alterarNome(nome: String) {
        perfil.value = perfil.value.copy(
            nome = nome
        )
    }

    // Alterar somente o e-mail
    fun alterarEmail(email: String) {
        perfil.value = perfil.value.copy(
            email = email
        )
    }
}

