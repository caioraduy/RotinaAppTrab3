package com.example.myapplication.viewmodel

// ╔══════════════════════════════════════════════╗
// ║  PerfilUiState.kt — pacote ui.perfil        ║
// ╚══════════════════════════════════════════════╝
import com.example.myapplication.data.local.Perfil



// Tudo o que a TelaPerfil e a TelaPerfilCont precisam para se desenhar
// (as duas telas usam o mesmo PerfilViewModel)
data class PerfilUiState(

    // perfil salvo no banco (o app guarda só 1)
    val perfil: Perfil = Perfil(),

    // true = já tem nome e e-mail -> mostra "Olá, nome!"
    val possuiCadastro: Boolean = false,

    // campos do formulário da TelaPerfilCont
    val nome: String = "",
    val email: String = "",

    // vira true quando terminou de salvar -> a TelaPerfilCont volta
    val perfilSalvo: Boolean = false
)
