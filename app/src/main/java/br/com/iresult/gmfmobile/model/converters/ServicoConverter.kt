package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Servico
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ServicoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToServicoList(data: String?): List<Servico> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Servico>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun servicoListToString(servicos: List<Servico>): String {
        return gson.toJson(servicos)
    }

    @TypeConverter
    fun fromString(value: String): Servico {
        val listType = object : TypeToken<Servico>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun servicoToString(servico: Servico): String {
        return Gson().toJson(servico)
    }

}