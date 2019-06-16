package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.OcorrenciaLeitura
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OcorrenciaLeituraConverter {

    @TypeConverter
    fun fromString(value: String): OcorrenciaLeitura {
        val listType = object : TypeToken<OcorrenciaLeitura>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun ocorrenciaLeituraToString(ocorrenciaLeitura: OcorrenciaLeitura): String {
        return Gson().toJson(ocorrenciaLeitura)
    }

}