package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import javax.annotation.Nullable

/**
 * Created by victorfernandes on 18/02/19.
 */
@Parcelize
@Entity(tableName = "roteiro")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Roteiro(
        @PrimaryKey @SerializedName("ARQUIVO") var nome: String,
        var porcentagem: Double?,
        @SerializedName("MES_REFERENCIA") var mes: String?,
        var totalRuas: Long?,
        var totalCasas: Long?,
        @SerializedName("PARA01")
        var parametros: List<Parametro>,
        @SerializedName("LIGA01")
        var tarefas: Tarefa?,
        @SerializedName("LIGA02")
        var leitura: List<Leitura>?,
        @SerializedName("TARI01")
        var tarifas: List<Tarifa>?,
        @SerializedName("CATE01")
        var categorias: List<Categoria>?,
        @SerializedName("RUBR01")
        var rubricas: List<Rubrica>?,
        @SerializedName("OCOR01")
        var ocorrencias: List<Ocorrencia>?,
        @SerializedName("JUST01")
        var justificativas: List<Justificativa>?,
        @SerializedName("SERV01")
        var servicos: List<Servico>?,
        @SerializedName("FAIX01")
        var faixas: List<Faixa>?,
        @SerializedName("DOMI01")
        var dominios: List<Dominio>?,
        @SerializedName("LIGA00")
        var parametroImpressao: ParametroImpressao?
) : Parcelable