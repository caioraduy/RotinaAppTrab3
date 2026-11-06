package com.example.myapplication.data.local

// ╔══════════════════════════════════════════════╗
// ║  AppDatabase.kt — pacote data.local         ║
// ╚══════════════════════════════════════════════╝

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Rotina::class, Atividade::class, Meta::class, Perfil::class],
    version = 1,
    // não gera o arquivo JSON do esquema (tira o aviso do Room no build)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // public static AppDatabase INSTANCE = null


    abstract fun rotinaDAO(): RotinaDAO

    abstract fun atividadeDAO(): AtividadeDAO

    abstract fun metaDAO(): MetaDAO

    abstract fun perfilDAO(): PerfilDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }else{

                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        // nome diferente do "app_database" do exemplo de Filmes:
                        // os dois apps usam o mesmo applicationId
                        // (com.example.myapplication), então com o mesmo nome
                        // o Room tentaria abrir o banco antigo dos Filmes e daria erro
                        "rotinapp_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }

            }

        }


    }

}
