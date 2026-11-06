package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.AtividadeRepository
import com.example.myapplication.data.repository.MetaRepository
import com.example.myapplication.data.repository.PerfilRepository
import com.example.myapplication.data.repository.RotinaRepository
import com.example.myapplication.ui.AppNavegacao
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // CAMADA DE DADOS:
        // o banco (Singleton) e um Repository para cada tabela.
        // Cada Repository recebe o seu DAO no construtor.
        // Assim, nenhuma tela (pacote ui) conhece o banco ou o DAO.
        val db = AppDatabase.getDatabase(applicationContext)

        val rotinaRepository = RotinaRepository(db.rotinaDAO())
        val atividadeRepository = AtividadeRepository(db.atividadeDAO())
        val metaRepository = MetaRepository(db.metaDAO())
        val perfilRepository = PerfilRepository(db.perfilDAO())

        setContent {
            MyApplicationTheme {
                AppNavegacao(
                    rotinaRepository = rotinaRepository,
                    atividadeRepository = atividadeRepository,
                    metaRepository = metaRepository,
                    perfilRepository = perfilRepository
                )
            }
        }
    }
}
