package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Justificativa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class JustificativaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToJustificativaList(data: String?): List<Justificativa> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Justificativa>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun justificativaListToString(justificativas: List<Justificativa>): String {
        return gson.toJson(justificativas)
    }

}