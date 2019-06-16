package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Roteiro
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by victorfernandes on 20/02/19.
 */
class RoteiroConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToRoteiroList(data: String?): List<Roteiro> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Roteiro>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun roteiroListToString(informativos: List<Roteiro>): String {
        return gson.toJson(informativos)
    }

}