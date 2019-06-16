package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "leitura")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Leitura(
        @PrimaryKey
        @SerializedName("NUM_REG") val numReg: Long = 0,
        @SerializedName("NUM_LIG") var numeroLigacao: Long = 0,
        @SerializedName("COD_GRU") var codigoGrupo: Int = 0,
        @SerializedName("MES_LAN") var mesLancamento: Int = 0,
        @SerializedName("ANO_LAN") var anoLancamento: Int = 0,
        @SerializedName("NUM_MATR") var numMat: Long = 0,
        @SerializedName("LTR_ATUAL") var leituraAtual: Int = 0,
        @SerializedName("COD_LEIT") var codLeitura: Int = 0,
        @SerializedName("COD_INT") var codLeituraInterno: Int = 0,
        @SerializedName("DAT_LEIT") var dataLeitura: String?,
        @SerializedName("HOR_LEIT") var horaLeitura: String?,
        @SerializedName("QTD_TENTA") var quantidadeTentativas: Int = 0,
        @SerializedName("CON_MEDIDO") var consumoMedido: Int = 0,
        @SerializedName("CON_MED_EE") var consumoMedidoEE: Int = 0,
        @SerializedName("CON_FAT_A") var conusumoFaturado: Int = 0,
        @SerializedName("CON_FAT_EE") var conusumoFaturadoEE: Int = 0,
        @SerializedName("VAL_TOTAL") var valorTotal: Double = 0.0,
        @SerializedName("QTD_CONS") var qtdCons: Int = 0,
        @SerializedName("CRT_FATUR") var criterioFaturamento: Int = 0,
        @SerializedName("DSC_STATUS") var descStatus: String?,
        @SerializedName("DSC_DICA") var descDica: String?,
        @SerializedName("DSC_OBS") var descObs: String?,
        @SerializedName("CRE_CON") var credConsumo: Int = 0,
        @SerializedName("TPO_ENTR") var tipoEntrega: String?,
        @SerializedName("DSC_MOT") var descMotivoEntrega: String?,
        @SerializedName("QTD_EMISS") var qtdEmissoes: Int = 0,
        @SerializedName("COD_EN_NOT") var codEntregaNotificacao: String?,
        @SerializedName("STA_BAT") var statusBateria: String?,
        @SerializedName("SQ_LEITURA") var sqLeitura: Long = 0,
        @SerializedName("VS_COLETOR") var versaoColetor: String?,
        @SerializedName("VAL_MINIMO") var valorMinimo: Double = 0.0,
        @SerializedName("STA_REG") var statusRegistro: Int = 0


): Parcelable
