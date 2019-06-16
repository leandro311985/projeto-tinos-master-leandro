package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Justificativa(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_OCOR") var codigo: Int,
        @SerializedName("DSC_JUSTIF") var descricao: String?
) : Parcelable