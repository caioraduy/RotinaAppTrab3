
package com.example.myapplication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.theme.Atividade
import com.example.myapplication.ui.theme.Rotina
import com.example.myapplication.ui.theme.calcularNivel
import com.example.myapplication.ui.theme.calcularXp
import com.example.myapplication.ui.theme.calcularXpNoNivel

class DesempenhoViewModel : ViewModel() {

    val rotinas = mutableStateOf<List<Rotina>>(emptyList())
    val atividades = mutableStateOf<List<Atividade>>(emptyList())

    // Antes: copiava das listas globais em memória
    // Agora: busca as rotinas e atividades DO BANCO
    // (chamada dentro de CoroutineScope(Dispatchers.IO).launch na TelaDesempenho)
    suspend fun atualizarDados(
        rotinaDAO: RotinaDAO,
        atividadeDAO: AtividadeDAO
    ) {

        rotinas.value = buscarRotinas(rotinaDAO)
        atividades.value = buscarAtividades(atividadeDAO)
    }

    fun quantidadeRotinas(): Int {
        return rotinas.value.size
    }

    fun quantidadeAtividades(): Int {
        return atividades.value.size
    }

    fun tempoTotal(): Double {
        return rotinas.value.sumOf {
            it.tempoMinutosRotina
        }
    }

    fun mediaTempoRotina(): Double {

        if (rotinas.value.isEmpty()) {
            return 0.0
        }

        return tempoTotal() / rotinas.value.size
    }

    fun maiorRotina(): Rotina? {

        return rotinas.value.maxByOrNull {
            it.tempoMinutosRotina
        }
    }

    fun menorRotina(): Rotina? {

        return rotinas.value.minByOrNull {
            it.tempoMinutosRotina
        }
    }

    fun atividadesDaRotina(idRotina: Int): List<Atividade> {

        return atividades.value.filter {
            it.idRotina == idRotina
        }
    }

    fun percentualDaRotina(rotina: Rotina): Float {

        val total = tempoTotal()

        if (total <= 0) {
            return 0f
        }

        return (rotina.tempoMinutosRotina / total).toFloat()
    }

    fun xp(): Int {
        return calcularXp(atividades.value)
    }

    fun nivel(): Int {
        return calcularNivel(atividades.value)
    }

    fun xpNoNivel(): Int {
        return calcularXpNoNivel(atividades.value)
    }

    fun progressoNivel(): Float {

        return xpNoNivel() / 100f
    }
}

