package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Tarifa(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_CATEG") var codigoCategoria: String?,
        @SerializedName("COD_FAT") var codigoFaturamento: Int,
        @SerializedName("VAL_MIN_A") var minimoAgua: Double = 0.0,
        @SerializedName("VAL_MINESA") var minimoEstimadoAgua: Double = 0.0,
        @SerializedName("VAL_MIN_E") var minimoEsgoto: Double = 0.0,
        @SerializedName("VAL_MINESE") var minimoEstimadoEsgoto: Double = 0.0,
        @SerializedName("VAL_MIN_T") var minimoTratamentoEsgoto: Double = 0.0,
        @SerializedName("VAL_MINEST") var minimoEstimadoTratamentoEsgoto: Double = 0.0
) : Parcelable