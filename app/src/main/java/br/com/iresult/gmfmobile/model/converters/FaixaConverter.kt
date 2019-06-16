package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Faixa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class FaixaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToFaixaList(data: String?): List<Faixa> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Faixa>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun faixaListToString(faixas: List<Faixa>): String {
        return gson.toJson(faixas)
    }

}