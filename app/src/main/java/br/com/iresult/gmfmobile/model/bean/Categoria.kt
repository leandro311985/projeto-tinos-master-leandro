package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Categoria(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_CATEG") val codCategoria: String,
        @SerializedName("DSC_CATEG") val descricao: String,
        @SerializedName("VAL_LIMITE") val valorLimite: Double,
        @SerializedName("VAL_MINIMO") val valorMinimo: Double
) : Parcelable