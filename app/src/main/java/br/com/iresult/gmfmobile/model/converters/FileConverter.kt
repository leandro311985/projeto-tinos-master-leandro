package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*


/**
 * Created by victorfernandes on 20/02/19.
 */
class FileConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToFileList(data: String?): List<File>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<File>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fileListToString(files: List<File>?): String {
        return gson.toJson(files)
    }

}