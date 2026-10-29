
package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.Historico
import com.example.myapplication.ui.theme.listaHistorico
import com.example.myapplication.ui.theme.removerDoHistorico

class HistoricoViewModel : ViewModel() {

    val historicos = listaHistorico

    fun removerHistorico(historico: Historico) {
        removerDoHistorico(historico)
    }
}
