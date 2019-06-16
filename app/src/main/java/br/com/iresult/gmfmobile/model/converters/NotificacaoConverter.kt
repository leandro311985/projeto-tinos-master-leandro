package br.com.iresult.gmfmobile.model.converters

import androidx.room.TypeConverter
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.Notificacao
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NotificacaoConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToNotificacaoList(data: String?): List<Notificacao> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Notificacao>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun notificacaoListToString(notificacoes: List<Notificacao>): String {
        return gson.toJson(notificacoes)
    }


    @TypeConverter
    fun stringToNotificacao(notificacao: String?) : Notificacao {
        return gson.fromJson(notificacao, Notificacao::class.java)
    }

    @TypeConverter
    fun notificacaoImpressaoToString(notificacao: Notificacao) : String {
        return gson.toJson(notificacao)
    }

}