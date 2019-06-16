package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.Coleta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ColetaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToColetaList(data: String?): List<Coleta> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Coleta>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun coletaListToString(categorias: List<Coleta>): String {
        return gson.toJson(categorias)
    }

}