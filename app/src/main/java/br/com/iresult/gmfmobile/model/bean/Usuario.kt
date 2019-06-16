package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "usuario")
data class Usuario(
        @PrimaryKey @SerializedName("COD_COLABORADOR") val codigo: String,
        @SerializedName("DSC_COLABORADOR") val nome: String,
        @SerializedName("STA_ATIVO") val status: String,
        @SerializedName("SIGLA_IDENTIFICADORA") val siglaIdentificadora: String
) : Parcelable