package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
data class ParametroImpressao(
        @PrimaryKey(autoGenerate = true) val uuid: Long,
        val numeroRegistro: Long,
        @SerializedName("SIGLA")  val sigla: String?,
        @SerializedName("QTD_REG") val qtdReg: Int?,
        @SerializedName("DAT_GER") val dataGer: String?,
        @SerializedName("HOR_GER") val horaGer: Int?,
        @SerializedName("MSG_CTA") val msgConta: String?,
        @SerializedName("REF_MED") val refMed: String?,
        @SerializedName("DSC_QMIN") val dscQmin: String?,
        @SerializedName("DSC_QMAX") val dscQmax: String?,
        @SerializedName("COD_RUBR") val cdRubrica: String?,
        @SerializedName("FAT_RED") val fatRedu: String?,
        @SerializedName("DSC_ALIQ") val descAli: String?,
        @SerializedName("STA_TRAT") val staTrat: String?,
        @SerializedName("DAT_TRAT") val dtTrat: String?,
        @SerializedName("ST_DIAPROP") val StDiaProp: String?,
        @SerializedName("QT_DIAFATU") val qtDiaFatu: Int?,
        @SerializedName("ST_CODBARR") val stCodBar: String?,
        @SerializedName("VL_CODBARR") val vlCodBar: Double?,
        @SerializedName("VOL_LIMITS") val volLimit: Int?,
        @SerializedName("NUM_CHAVE") val numChave: Int?,
        @SerializedName("FXA_LAN01") val fxaLanc1: String?,
        @SerializedName("FXA_LAN02") val fxaLanc2: String?,
        @SerializedName("FXA_LAN03") val fxaLanc3: String?,
        @SerializedName("FXA_LAN04") val fxaLanc4: String?,
        @SerializedName("FXA_LAN05") val fxaLanc5: String?,
        @SerializedName("FXA_LAN06") val fxaLanc6: String?,
        @SerializedName("FXA_LAN07") val fxaLanc7: String?,
        @SerializedName("FXA_LAN08") val fxaLanc8: String?,
        @SerializedName("FXA_LAN09") val fxaLanc9: String?,
        @SerializedName("FXA_LAN10") val fxaLanc10: String?,
        @SerializedName("FXA_LAN11") val fxaLanc11: String?,
        @SerializedName("FXA_LAN12") val fxaLanc12: String?,
        @SerializedName("FXA_LAN13") val fxaLanc13: String?,
        @SerializedName("FXA_LAN14") val fxaLanc14: String?,
        @SerializedName("FXA_LAN15") val fxaLanc15: String?,
        @SerializedName("FXA_LAN16") val fxaLanc16: String?,
        @SerializedName("FXA_LAN17") val fxaLanc17: String?,
        @SerializedName("FXA_LAN18") val fxaLanc18: String?,
        @SerializedName("FXA_LAN19") val fxaLanc19: String?,
        @SerializedName("FXA_LAN20") val fxaLanc20: String?
) : Parcelable
