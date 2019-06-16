package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TipoOcorrencia(
        @PrimaryKey(autoGenerate = true) val uuid: Long?,
        @SerializedName("codigo") val codigo: String,
        @SerializedName("descricao") val descricao: String
) : Parcelable