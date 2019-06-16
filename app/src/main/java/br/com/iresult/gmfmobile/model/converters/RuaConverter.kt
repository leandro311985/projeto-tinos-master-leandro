package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by victorfernandes on 20/02/19.
 */
class RuaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToRuaList(data: String?): List<Address> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Address>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ruaListToString(addresses: List<Address>): String {
        return gson.toJson(addresses)
    }

}