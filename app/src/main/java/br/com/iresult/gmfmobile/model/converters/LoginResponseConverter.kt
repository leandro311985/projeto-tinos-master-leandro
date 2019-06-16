package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.LoginResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LoginResponseConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToLoginResponseList(data: String?): List<LoginResponse> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<LoginResponse>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun loginResponseListToString(loginResponses: List<LoginResponse>): String {
        return gson.toJson(loginResponses)
    }

}