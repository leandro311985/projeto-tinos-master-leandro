package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Ocorrencia
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by victorfernandes on 20/02/19.
 */
class OcorrenciaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToOcorrenciaList(data: String?): List<Ocorrencia> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Ocorrencia>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ocorrenciaListToString(ocorrencias: List<Ocorrencia>): String {
        return gson.toJson(ocorrencias)
    }

    @TypeConverter
    fun fromString(value: String): Ocorrencia {
        val listType = object : TypeToken<Ocorrencia>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun ocorrenciaToString(ocorrencia: Ocorrencia): String {
        return Gson().toJson(ocorrencia)
    }

}