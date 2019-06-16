package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class CategoriaConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToCategoriaList(data: String?): List<Categoria> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Categoria>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun categoriaListToString(categorias: List<Categoria>): String {
        return gson.toJson(categorias)
    }

}