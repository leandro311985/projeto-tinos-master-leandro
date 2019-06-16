package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.TipoOcorrencia
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TipoOcorrenciaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToTipoOcorrenciaList(data: String?): List<TipoOcorrencia> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<TipoOcorrencia>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tipoOcorrenciaListToString(tipoOcorrencias: List<TipoOcorrencia>): String {
        return gson.toJson(tipoOcorrencias)
    }

}