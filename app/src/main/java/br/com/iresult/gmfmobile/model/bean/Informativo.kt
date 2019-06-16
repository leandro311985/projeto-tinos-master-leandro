package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by victorfernandes on 18/02/19.
 */
@Entity(tableName = "informativo")

data class Informativo(
        @PrimaryKey val uuid: String,
        @SerializedName("imagem") val imagem: String?,
        @SerializedName("descricao") val descricao: String?
)