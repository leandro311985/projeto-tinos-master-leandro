package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Tarefa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by victorfernandes on 20/02/19.
 */
class TarefaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToTarefaList(data: String?): List<Tarefa> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Tarefa>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tarefaListToString(informativos: List<Tarefa>): String {
        return gson.toJson(informativos)
    }

    @TypeConverter
    fun stringToTarefa(data: String?) : Tarefa {
        return gson.fromJson(data, Tarefa::class.java)
    }

    @TypeConverter
    fun tarefaToString(tarefa: Tarefa) : String {
        return gson.toJson(tarefa)
    }

}