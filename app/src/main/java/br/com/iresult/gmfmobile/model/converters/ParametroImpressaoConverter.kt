package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import br.com.iresult.gmfmobile.model.bean.Tarefa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ParametroImpressaoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToParametroImpressaoList(data: String?): List<ParametroImpressao> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<ParametroImpressao>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun parametroImpressaoListToString(parametroImpressao: List<ParametroImpressao>): String {
        return gson.toJson(parametroImpressao)
    }

    @TypeConverter
    fun stringToParametroImpressao(parametroImpressao: String?) : ParametroImpressao {
        return gson.fromJson(parametroImpressao, ParametroImpressao::class.java)
    }

    @TypeConverter
    fun parametroImpressaoToString(parametroImpressao: ParametroImpressao) : String {
        return gson.toJson(parametroImpressao)
    }

}