package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.ui.theme.Atividade
import com.example.myapplication.ui.theme.Meta
import com.example.myapplication.ui.theme.Perfil
import com.example.myapplication.ui.theme.Rotina

@Database(
    entities = [Rotina::class, Atividade::class, Meta::class, Perfil::class],
    version = 1
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
