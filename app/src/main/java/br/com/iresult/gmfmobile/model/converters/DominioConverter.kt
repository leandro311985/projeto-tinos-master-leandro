package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.Dominio
import br.com.iresult.gmfmobile.model.bean.Tarefa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DominioConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToDominioList(data: String?): List<Dominio> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Dominio>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun dominioListToString(dominio: List<Dominio>): String {
        return gson.toJson(dominio)
    }

    @TypeConverter
    fun stringToDominio(dominio: String?) : Dominio {
        return gson.fromJson(dominio, Dominio::class.java)
    }

    @TypeConverter
    fun dominioToString(dominio: Dominio) : String {
        return gson.toJson(dominio)
    }

}