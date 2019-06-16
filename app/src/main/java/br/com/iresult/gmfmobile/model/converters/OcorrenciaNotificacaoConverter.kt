package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.OcorrenciaNotificacao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class OcorrenciaNotificacaoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToOcorrenciaNotificacaoList(data: String?): List<OcorrenciaNotificacao> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<OcorrenciaNotificacao>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ocorrenciaNotificacaoListToString(ocorrenciaNotificacoes: List<OcorrenciaNotificacao>): String {
        return gson.toJson(ocorrenciaNotificacoes)
    }

}