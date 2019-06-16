package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Informativo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


/**
 * Created by victorfernandes on 20/02/19.
 */
class InformativoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToInformativoList(data: String?): List<Informativo> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Informativo>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun informativoListToString(informativos: List<Informativo>): String {
        return gson.toJson(informativos)
    }

}