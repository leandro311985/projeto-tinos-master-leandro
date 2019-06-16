package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.bean.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HashMapConverter {

    @TypeConverter
    fun fromString(value: String): HashMap<Address, Ligacao> {
        val listType = object : TypeToken<HashMap<Address, Ligacao>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun hashMapToString(hashmap: HashMap<Address, Ligacao>): String {
        return Gson().toJson(hashmap)
    }

}