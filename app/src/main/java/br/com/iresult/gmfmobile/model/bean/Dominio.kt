package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Dominio(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("NOM_DOM") val nomeDominio: String,
        @SerializedName("VAL_DOM") val valorDominio: String,
        @SerializedName("DSC_DOM") val descricao: String
) : Parcelable