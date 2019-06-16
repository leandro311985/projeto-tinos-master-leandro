package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.File

@Entity
@Parcelize
data class Ocorrencia(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_OCOR") var codigo: Int,
        @SerializedName("DSC_OCOR") var descricao: String?,
        @SerializedName("MSG_PREV") var mensagemPrevencao: String?,
        @SerializedName("STA_ACEITA") var statusAceita: String?,
        @SerializedName("STA_JUSTIF") var statusJustificativa: String?,
        @SerializedName("STA_ESCOND") var statusEscondido: String?,
        @SerializedName("STA_ROL") var statusRepasse: String?,
        @SerializedName("STA_CRIT") var statusCriterio: String?,
        @SerializedName("STA_EMIS") var statusEmissao: String?,
        @SerializedName("CRT_FAT") var criterioFaturamento: String,
        @SerializedName("MSG_LEIT") var mensagemLeitura: String?
) : Parcelable {

    companion object {

        const val STATUS_IMPEDITIVO = "N"
        const val STATUS_NAO_IMPEDITIVO = "S"
        const val STATUS_JUSTIFICADO = "S"
        const val STATUS_NAO_JUSTIFICADO = "N"
        const val STATUS_ESCONDE = "S"
        const val STATUS_NAO_ESCONDE = "N"

    }

    fun necessitaJusticativa() : Boolean {
        return statusJustificativa == "S"
    }

    fun isOcorrenciaImpeditiva() : Boolean {
        return statusAceita == STATUS_IMPEDITIVO
    }

}