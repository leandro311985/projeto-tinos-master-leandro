package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Ligacao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by victorfernandes on 20/02/19.
 */
class LigacaoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToUnidadeList(data: String?): List<Ligacao> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Ligacao>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun unidadeListToString(ligacaos: List<Ligacao>): String {
        return gson.toJson(ligacaos)
    }

    @TypeConverter
    fun stringToUnidade(data: String?): Ligacao {
        return gson.fromJson(data, Ligacao::class.java)
    }

    @TypeConverter
    fun unidadeToString(ligacao: Ligacao): String {
        return gson.toJson(ligacao)
    }

}