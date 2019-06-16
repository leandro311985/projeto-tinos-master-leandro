package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.Leitura
import br.com.iresult.gmfmobile.model.bean.Tarefa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LeituraConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToLeituraList(data: String?): List<Leitura> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Leitura>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun leituraListToString(leitura: List<Leitura>): String {
        return gson.toJson(leitura)
    }

    @TypeConverter
    fun stringToLeitura(leitura: String?) : Leitura {
        return gson.fromJson(leitura, Leitura::class.java)
    }

    @TypeConverter
    fun leituraToString(leitura: Leitura) : String {
        return gson.toJson(leitura)
    }

}