package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.ServicoLeitura
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ServicoLeituraConverter {

    @TypeConverter
    fun fromString(value: String): ServicoLeitura {
        val listType = object : TypeToken<ServicoLeitura>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun servicoLeituraToString(servicoLeitura: ServicoLeitura): String {
        return Gson().toJson(servicoLeitura)
    }

}