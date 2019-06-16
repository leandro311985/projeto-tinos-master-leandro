package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "coleta")
@Parcelize
data class Coleta(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: Int?,
        @SerializedName("NUM_LIG") val descricao: Int?,
        @SerializedName("COD_GRU") val codGru: Int?,
        @SerializedName("MES_LAN") val mesLan: Int?,
        @SerializedName("ANO_LAN") val anoLan: Int?,
        @SerializedName("NUM_MATR") val numMatr: Int?,
        @SerializedName("LTR_ATUAL") val ltrAtual: Int?,
        @SerializedName("COD_LEIT") val codLeit: Int?,
        @SerializedName("COD_INT") val codInt: Int?,
        @SerializedName("DAT_LEIT") val datLeit: String?,
        @SerializedName("HOR_LEIT") val horaLeit: String?,
        @SerializedName("QTD_TENTA") val qtdTenta: Int?,
        @SerializedName("CON_MEDIDO") val conMedido: Int?,
        @SerializedName("CON_MED_EE") val conMedEE: Int?,
        @SerializedName("CON_FAT_A") val conFatA: Int?,
        @SerializedName("CON_FAT_EE") val conFatEE: Int?,
        @SerializedName("VAL_TOTAL") val ValTotal: Double? = 0.0,
        @SerializedName("QTD_CONS") val QtdCons: Int?,
        @SerializedName("CRT_FATUR") val crtFatur: Int?,
        @SerializedName("DSC_STATUS") val dscStatus: String?,
        @SerializedName("DSC_DICA") val dscDica: String?,
        @SerializedName("DSC_OBS") val dscObs: String?,
        @SerializedName("CRE_CON") val creCon: Int?,
        @SerializedName("TPO_ENTR") val tpoEntr: String?,
        @SerializedName("DSC_MOT") val dscMot: String?,
        @SerializedName("QTD_EMISS") val qtdEmiss: Int?,
        @SerializedName("COD_EN_NOT") val codEnNot: Int?,
        @SerializedName("STA_BAT") val stabat: Int?,
        @SerializedName("SQ_LEITURA") val sqLeitura: Int?,
        @SerializedName("VS_COLETOR") val vsColetor: String?,
        @SerializedName("VAL_MINIMO") val valMinimo: Double? = 0.0,
        @SerializedName("STA_REG") val staReg: String

) : Parcelable