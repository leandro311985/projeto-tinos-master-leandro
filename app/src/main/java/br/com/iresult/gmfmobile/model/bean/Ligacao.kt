package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by victorfernandes on 18/02/19.
 */
@Parcelize
@Entity(tableName = "unidade")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Ligacao(
        @PrimaryKey
        @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("NUM_LIGA") var numeroLigacao: Long = 0,
        @SerializedName("NUM_INSC") var numeroInscricao: String?,
        @SerializedName("END_LIGA") var enderecoLigacao: String?,
        @SerializedName("RUA") var rua: String?,
        @SerializedName("NUMERO") var numero: String?,
        @SerializedName("BAIRRO") var bairro: String?,
        @SerializedName("CIDADE") var cidade: String?,
        @SerializedName("UF") var uf: String?,
        @SerializedName("CEP") var cep: String?,
        @SerializedName("DSC_COMP") var descricaoComplemento: String?,
        @SerializedName("NOM_CLI") var nomeCliente: String?,
        @SerializedName("DSC_DICA") var dica: String?,
        @SerializedName("NUM_MED") var numeroMedidor: String?,
        var zona: String?,
        var setor: String?,
        var grupo: String?,
        @SerializedName("ROTEIRO") var roteiro: String?,
        var rota: String?,
        var quadra: String?,
        @SerializedName("LEITURA") var leituraAnterior: String,
        var leituraAnteriorInt: Int = 0,
        var leituraMaxima: Int = 0,
        var leituraInstalacao: Int = 0,
        var dataLeituraAnterior: String?,
        var dataInstalacao: String?,
        var dataVencimento: String?,
        var consumoResidual: Int = 0,
        var consumoMinimo: Int = 0,
        var consumoMinimoA: Int = 0,
        var consumoMinimoB: Int = 0,
        var consumoMaximoA: Int = 0,
        var consumoMaximoB: Int = 0,
        var consumoMedio: Int = 0,
        var consumoMedioFaturado: Int = 0,
        var consumoEstimado: Int = 0,
        var consumoPipa: Int = 0,

        @SerializedName("DATA") var data: String?,

        @SerializedName("CONSUMO") var consumo: String?,
        @SerializedName("CONSUMO2") var consumo2: String?,
        // CRE_CONSUMO(5)(371)+VOL_AREA(6)(376)+CON_FATURADO_EE(7)(382)
        @SerializedName("CRE_CONSUMO") var creditoConsumo: Int? = 0,
        @SerializedName("VOL_AREA") var volumeArea: Int? = 0,
        @SerializedName("CON_FATURADO_EE") var consumoFaturadoEsgotoEspecial: Int? = 0,

        @SerializedName("STATUS") var statusGrupo: String?,
        // STA_TROCA(1)(389)+STA_LIGA_NOVA(1)(390)+TPO_LIGACAO(1)(391)+SIT_LIGACAO(1)(392)+STA_GRANDE(1)(393)+STA_ECONOMIA(1)(394)+TPO_IMPOSTO(1)(395)+TPO_LANCAMENTO(1)(396)+STA_TRATAMENTO_ESGOTO(1)
        @SerializedName("STA_TROCA") var statusTroca: String?,
        @SerializedName("STA_LIGA_NOVA") var statusLigacaoNova: String?,
        @SerializedName("TPO_LIGACAO") var tipoLigacao: Int = 0,
        @SerializedName("SIT_LIGACAO") var situacaoLigacao: String?,
        @SerializedName("STA_GRANDE") var statusGrande: String?,
        @SerializedName("STA_ECONOMIA") var statusEconomia: String?,
        @SerializedName("TPO_IMPOSTO") var tipoImposto: String?,
        @SerializedName("TPO_LANCAMENTO") var tipoLancamento: String?,
        @SerializedName("STA_TRATAMENTO_ESGOTO") var statusTratamentoEsgoto: String?,

        @SerializedName("ECONOMIA") var economia: String?,
        // QTD_ECO_DOMICILIAR(4)(397)+QTD_ECO_COMERCIAL(4)(401)+QTD_ECO_INDUSTRIAL(4)(405)+QTD_ECO_PUBLICA(4)(409)+QTD_ECO_OUTRAS(4)(413)
        @SerializedName("QTD_ECO_DOMICILIAR") var economiaResidencial: Int = 0,
        @SerializedName("QTD_ECO_COMERCIAL") var economiaComercial: Int = 0,
        @SerializedName("QTD_ECO_INDUSTRIAL") var economiaIndustrial: Int = 0,
        @SerializedName("QTD_ECO_PUBLICA") var economiaPublica: Int = 0,
        @SerializedName("QTD_ECO_OUTRAS") var economiaOutras: Int = 0,
        var totalEconomias: Int = 0,

        @SerializedName("CODIGO") var codigo: String?,
        // COD_ARRECAD(3)(417)+NUM_AGENCIA(4)(420)+CTA_CORRENTE(14)(424)+COD_REMESSA(1)(438)+COD_COBRANCA(1)(439)+COD_CATEGORIA(2)(440)+COD_FATURAMENTO(1)(442)+COD_TRIBUTO(3)(443)+CRT_FATURAMENTO(2)(446)
        var codigoArrecadacao: String?,
        var numeroAgencia: String?,
        var contaCorrente: String?,
        var codigoRemessa: Int = 0,
        var codigoCobranca: Int = 0,
        var codigoCategoria: String?,
        var codigoFaturamento: Int = 0,
        var codigoTributo: Int = 0,
        var criterioFaturamento: Int = 0,

        @SerializedName("COD_PREV") var codigoPrevencao: Int? = 0,

        @SerializedName("DSC_EXCE") var dscExce: String?,

        // CRT_EXCECAO(2)(451)+VOL_LIMITE(7)(453)+PER_ESGOTO(6)(460)
        var criterioExcecao: Int = 0,
        var volumeLimite: Int = 0,
        var percentualEsgoto: Int = 0,

        @SerializedName("NUM_AVIS") var numAvis: String?,
        // NUM_AVISO(9)(466) + ANO_LANCTO(4)(475) + NUM_EMISSAO(2)(479)
        var numeroAviso: Long = 0,
        var anoLancamento: Int = 0,
        var numeroEmissao: Int = 0,

        // c�digos de barras
        @SerializedName("COD_BARR") var codigoBarras: String?,
        var codigoBarrasNotificacao: String?,
        @SerializedName("DSC_HIST") var historicoConsumo: String?,

        // MENSAGEM DE D�BITO(77)(681) + MSG_PERSONALIZADA(70)(758)
        @SerializedName("DSC_MSG") var mensagemDebito: String?,
        var mensagemPersonalizada: String?,
        var mensagemLeiFederal: String?,
        @SerializedName("NUM_CONT") var controle: String?,
        var controleNotificacao: String?,
        @SerializedName("LIGA_DIG") var ligacaoDigito: String?,
        @SerializedName("STA_OCOR") var statusOcorrencia: String?,
        @SerializedName("STA_IMPEDE") var statusImpedimento: String?,
        @SerializedName("MSG_IMPEDE") var mensagemImpedimento: String?,
        @SerializedName("STA_REG") var statusRegistro: String,
        @SerializedName("DAT_PROX") var dataProximaLeitura: String?,
        @SerializedName("STA_EX_FAT") var staExcecaoFat: String?,
        var credNegativoFatur: Int = 0,
        @SerializedName("STA_EXCSOC") var excecaoSocial: String?,
        var concluido: Boolean,

        /**
         * Variavel auxiliar para definir status das casas
         *
         * CONCLUIDO
         * NAO_ENTREGUE
         * AGORA
         * PROXIMO
         */
        var status: String?,
        val latitude: Double?,
        val longitude: Double?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ligacao

        if (numReg != other.numReg) return false

        return true
    }

    override fun hashCode(): Int {
        return numReg.hashCode()
    }

    fun getCompleteAddress(cep: Boolean = true): String {
        return if (cep) {
            "$rua, $numero, cep $cep, $cidade - $uf"
        } else {
            "$rua, $numero, $cidade - $uf"
        }
    }

}