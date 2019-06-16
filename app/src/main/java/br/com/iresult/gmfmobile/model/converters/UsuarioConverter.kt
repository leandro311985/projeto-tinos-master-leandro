package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class UsuarioConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToUsuario(data: String?) : Usuario {
        return gson.fromJson(data, Usuario::class.java)
    }

    @TypeConverter
    fun usuarioToString(usuario: Usuario) : String {
        return gson.toJson(usuario)
    }

}