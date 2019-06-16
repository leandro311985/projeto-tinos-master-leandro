package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Rubrica(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: Int,
        @SerializedName("COD_RUBR") var codigoRubrica: Int = 0,
        @SerializedName("DSC_RUBR") var descricaoRubrica: String?,
        @SerializedName("STA_REF") var statusReferencia: String?
) : Parcelable