package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Parametro(
        @SerializedName("NUM_REG") @PrimaryKey val numReg: String,
        @SerializedName("SEQ_CRON") val seqCrono: String?,
        @SerializedName("NOM_COLE") val nome: String?,
        @SerializedName("NUM_CONTRO") val numConrtole: Long?,
        @SerializedName("DAT_INI") val dataIni: String?,
        @SerializedName("DAT_FIM") val dataFim: String?,
        @SerializedName("NUM_ROTA") val numRota: Long?,
        @SerializedName("DSC_VERSAO") val versao: String?,
        @SerializedName("MSG1") val mensagem1: String?,
        @SerializedName("MSG2") val mensagem2: String?,
        @SerializedName("MSG3") val mensagem3: String?
) : Parcelable
