package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Servico(
        @PrimaryKey @SerializedName("NUM_REG") var numReg: Long,
        @SerializedName("NUM_LSER") var matricula: Int = 0,
        @SerializedName("NUM_AVIS") var numeroAviso: Long = 0,
        @SerializedName("ANO_LAN") var anoEmissao: Int = 0,
        @SerializedName("NUM_EMIS") var numeroEmissao: Int = 0,
        @SerializedName("COD_TRIBU") var codigoTributo: Int = 0,
        @SerializedName("NUM_SERV") var numeroServico: Int = 0,
        @SerializedName("ITM_SERV") var itemServico: Int = 0,
        @SerializedName("COD_RUBR") var codigoRubrica: Int = 0,
        @SerializedName("VAL_PARCEL") var valorParcela: Double = 0.toDouble(),
        @SerializedName("VAL_IMPOST") var valorImposto: Double = 0.toDouble(),
        @SerializedName("VAL_BASE_I") var valorBaseImposto: Double = 0.toDouble(),
        @SerializedName("REF_PARCEL") var referenciaParcela: String?,
        @SerializedName("STA_DESC") var statusDesconto: String?,
        @SerializedName("VAL_SALDO") var saldoServico: Double = 0.toDouble(),
        @SerializedName("STA_LANC") var statusLancamento: String?,
        var quantidadeServicos: Int = 0
) : Parcelable