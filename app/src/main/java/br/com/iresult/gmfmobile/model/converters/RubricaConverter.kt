package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Rubrica
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RubricaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToRubricaList(data: String?): List<Rubrica> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Rubrica>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun rubricaListToString(rubricas: List<Rubrica>): String {
        return gson.toJson(rubricas)
    }

    @TypeConverter
    fun stringToRubrica(data: String?): Rubrica {
        return gson.fromJson(data, Rubrica::class.java)
    }

    @TypeConverter
    fun rubricaToString(rubrica: Rubrica): String {
        return gson.toJson(rubrica)
    }

}