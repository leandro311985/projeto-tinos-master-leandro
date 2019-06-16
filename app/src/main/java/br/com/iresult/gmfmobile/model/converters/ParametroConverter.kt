package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Parametro
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ParametroConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToParametroList(data: String?): List<Parametro> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Parametro>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun parametroListToString(parametros: List<Parametro>): String {
        return gson.toJson(parametros)
    }

}