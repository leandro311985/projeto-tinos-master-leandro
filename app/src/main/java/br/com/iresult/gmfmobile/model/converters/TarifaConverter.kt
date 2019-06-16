package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Tarifa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TarifaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToTarifaList(data: String?): List<Tarifa> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Tarifa>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tarifaListToString(tarifas: List<Tarifa>): String {
        return gson.toJson(tarifas)
    }

}