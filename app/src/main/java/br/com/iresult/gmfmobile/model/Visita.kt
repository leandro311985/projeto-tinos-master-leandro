package br.com.iresult.gmfmobile.model

import android.util.Log
import br.com.iresult.gmfmobile.model.bean.*

import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Vector

import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.utils.NumberUtils
import br.com.iresult.gmfmobile.utils.Utils
import br.com.iresult.gmfmobile.utils.defaultScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.danlew.android.joda.JodaTimeAndroid
import java.text.DecimalFormat

class Visita {

    val numeroRegistro: Int = 0

    var numeroLigacao: Long = 0

    val codigoGrupo: Int = 0

    val mesLancamento: Int = 0

    val anoLancamento: Int = 0

    val matricula: Int = 0

    var leituraAtual: Int = 0

    var leituraAnterior: Int = 0

    val ultimaDigitada: Int = 0

    val codigoLeitura: Int = 0

    var codigoLeituraInterno: Int = 0

    val dataLeitura: Date? = Date()

    val horarioLeitura: String? = null

    var consumoMedido: Int = 0

    var consumoMedidoEsgoto: Int = 0

    var consumoFaturadoAgua: Int = 0

    var cascata: Cascata?   = Cascata()

    var consumoFaturadoEsgoto: Int = 0

    var excecaoConsumoFaturadoEsgoto: Int = 0

    var valorTotal: Double = 0.toDouble()

    var statusVirada = "N"

    var statusRepasse: String? = "N"

    val dica: String? = null

    val descricaoObservacao: String? = null

    val observacaoEntrega: String? = null

    var creditoConsumo: Int = 0

    var criterioFaturamento: Int = 0

    val tipoEntrega: String? = null

    var mensagemEntrega = ""

    val motivoEntrega: String? = null

    val quantidadeEmissao: Int = 0

    val codigoEntregaNotificacao: Int = 0

    var statusRegistro: Int = 0

    var ligacao: Ligacao? = null

    val ocorrencia: Ocorrencia? = null

    var tarefa: Coleta? = null

    // variáveis que não são campos da tabela
    var ocorrenciaImpeditiva = false

    var quantidadeDiasConsumo: Int = 0

    var quantidadeTentativas: Int = 0

    val codigoMotivo: Int = 0

    val descricaoMotivo = StringUtils.leftPad("", 30, " ")

    var statusEmissao: String? = null

    var faixas: List<Faixa> = ArrayList<Faixa>()

    var receita: String? = null

    private var minimoTarifa = 0
    private var minimoTarifaResidencial = 0
    private var minimoTarifaComercial = 0
    private var minimoTarifaIndustrial = 0
    private var minimoTarifaPublica = 0
    private var minimoTarifaOutras = 0

    private var minimoAgua = 0.0
    private var minimoEsgoto = 0.0
    private var minimoEsgotoEspecial = 0.0
    private var minimoTratamento = 0.0

    private var statusErro = 0

    private var aguaResidencial = 0.0
    private var aguaComercial = 0.0
    private var aguaIndustrial = 0.0
    private var aguaPublica = 0.0
    private var aguaOutras = 0.0

    private var esgotoResidencial = 0.0
    private var esgotoComercial = 0.0
    private var esgotoIndustrial = 0.0
    private var esgotoPublica = 0.0
    private var esgotoOutras = 0.0

    private var tratamentoResidencial = 0.0
    private var tratamentoComercial = 0.0
    private var tratamentoIndustrial = 0.0
    private var tratamentoPublica = 0.0
    private var tratamentoOutras = 0.0

    private var valorMinimo = 0.0

    private var valorAgua = 0.0
    private var valorEsgoto = 0.0
    private var valorEsgotoEspecial = 0.0
    private var valorTratamentoEsgoto = 0.0

    // inicializadas no Calcula Consumo
    private var quantidadeLancamentos: Int = 0
    var quantidadeFaixas: Int = 0
    private val imprimeFaixas = HashMap<Int, String>()
    private val lancamentos = HashMap<Int, String>()

    private var valorNegado = 0.0

    private var demaisLancamentos = 0.0
    private var demaisLancamentosNegados = 0.0

    // começa sem impedimento
    private val impedimento = 1
    private var impedimentoEstatistica: Int = 0
    private var lidos: Int = 0

    // impressão
    private var faixaTratamentoEsgoto: Double = 0.toDouble()

    var seqLeitura: Long = 0

    private val staBateria: Int = 0

    private var quantidadeDiasTratEsgoto = 0

    private var statusTratEsgotoProporcional: String? = null

    var valorMinNaoLancado: Double = 0.toDouble()

    private val staExcecaoFat: String? = null

    private var dataBase: AppDataBase? = null


    // Cobrar credito negativo apenas de ligações com consumo medido > 0
    private val isCalcularCredito: Boolean
        get() = if ((ligacao!!.creditoConsumo?.compareTo(0)!! < 0) && this.consumoMedido == 0) {
            false
        } else {
            true
        }


    private// Criterio de exceção FATURAR TARIFA RESIDENCIAL //
    val isExcecaoRES: Boolean
        @Throws(Exception::class)
        get() = if ((ligacao!!.codigoCategoria == "R1"
                        && ligacao!!.criterioExcecao == 55
                        && ((this.consumoMedido > this.tarefa!!.valorLimiteSocial || ligacao!!.excecaoSocial == "S")))) {

            true
        } else false

    /*
        * Verifica se existe ocorrência impeditiva
       */

    val isOcorrenciaImpeditiva: Boolean
        get() {

            if (!this.ocorrenciaImpeditiva) {
                if (this.ocorrencia != null && this.ocorrencia!!.statusAceita == Ocorrencia.STATUS_IMPEDITIVO) {
                    this.ocorrenciaImpeditiva = true
                }
            }

            return this.ocorrenciaImpeditiva
        }


    /** Verifica se a Justificativa é obrigatória  */

    val isRequeridoJustif: Boolean
        get() {

            var obrigatoria = false

            if (this.ocorrencia != null && this.ocorrencia.statusJustificativa == Ocorrencia.STATUS_JUSTIFICADO) {
                obrigatoria = true
            }

            return obrigatoria
        }

    constructor() {}

    constructor(dataBase: AppDataBase?, ligacao: Ligacao?, leitura: String?) {
        this.dataBase = dataBase
        this.ligacao = ligacao
        iniciarLigarcao(ligacao)
        if (StringUtils.isNotBlank(leitura)){
            this.leituraAtual = leitura?.toInt()!!
        }
    }

    private fun iniciarLigarcao(ligacao: Ligacao?) {
        this.numeroLigacao = ligacao?.numeroLigacao!!
        ligacao?.zona = ligacao?.roteiro!!.substring(0, 3)
        ligacao?.setor = ligacao?.roteiro!!.substring(3, 6)
        ligacao?.grupo = ligacao?.roteiro!!.substring(6, 9)
        ligacao?.rota = ligacao?.roteiro!!.substring(9, 13)
        ligacao?.quadra = ligacao?.roteiro!!.substring(13, 16)


        ligacao?.leituraAnteriorInt = Integer
                .parseInt(ligacao?.leituraAnterior.substring(0, 7))
        ligacao?.leituraMaxima = Integer
                .parseInt(ligacao?.leituraAnterior.substring(7, 14))
        ligacao?.leituraInstalacao = Integer
                .parseInt(ligacao?.leituraAnterior.substring(14, 21))

        ligacao?.dataLeituraAnterior = (ligacao?.data!!.substring(6,
                8)
                + "/"
                + ligacao?.data!!.substring(4, 6)
                + "/"
                + ligacao?.data!!.substring(0, 4))

        ligacao?.dataInstalacao = (ligacao?.data!!.substring(14, 16)
                + "/" + ligacao?.data!!.substring(12, 14) + "/"
                + ligacao?.data!!.substring(8, 12))

        ligacao?.dataVencimento = (ligacao?.data!!.substring(22, 24)
                + "/" + ligacao?.data!!.substring(20, 22) + "/"
                + ligacao?.data!!.substring(16, 20))

        ligacao.consumoResidual = Integer
                .parseInt(ligacao?.consumo!!.substring(0, 7))
        ligacao.consumoMinimo = Integer
                .parseInt(ligacao?.consumo!!.substring(7, 14))
        ligacao.consumoMinimoA = Integer
                .parseInt(ligacao?.consumo!!.substring(14, 21))
        ligacao.consumoMinimoB = Integer
                .parseInt(ligacao?.consumo!!.substring(21, 28))
        ligacao.consumoMaximoA = Integer
                .parseInt(ligacao?.consumo!!.substring(28, 35))
        ligacao.consumoMaximoB = Integer
                .parseInt(ligacao?.consumo!!.substring(35, 42))
        ligacao.consumoMedio = Integer
                .parseInt(ligacao?.consumo!!.substring(42, 49))
        ligacao?.consumoMedioFaturado = Integer
                .parseInt(ligacao?.consumo!!.substring(49, 56))
        ligacao?.consumoEstimado = Integer
                .parseInt(ligacao?.consumo!!.substring(56, 63))
        ligacao?.consumoPipa = Integer
                .parseInt(ligacao?.consumo!!.substring(63, 70))


        val cred = ligacao?.consumo2!!.substring(0, 5)
        ligacao?.creditoConsumo = Integer
                .parseInt(cred.substring(if (cred.indexOf("-") == -1) 1 else cred.indexOf("-"), cred.length))

        ligacao?.credNegativoFatur = if (ligacao?.creditoConsumo!! < 0) ligacao?.creditoConsumo!! else 0

        ligacao?.volumeArea = Integer.parseInt(ligacao?.consumo2!!.substring(5, 11))

        ligacao?.consumoFaturadoEsgotoEspecial = Integer.parseInt(ligacao?.consumo2!!.substring(11, 18))

        ligacao?.statusTroca = ligacao?.statusGrupo!!.substring(0, 1)

        ligacao?.statusLigacaoNova = ligacao?.statusGrupo!!.substring(1, 2)

        ligacao?.tipoLigacao = Integer.parseInt(ligacao?.statusGrupo!!.substring(2, 3))

        ligacao?.situacaoLigacao = ligacao?.statusGrupo!!.substring(3, 4)

        ligacao?.statusGrande = ligacao?.statusGrupo!!.substring(4, 5)
        ligacao?.statusEconomia = ligacao?.statusGrupo!!.substring(5, 6)
        ligacao?.tipoImposto = ligacao?.statusGrupo!!.substring(6, 7)
        ligacao?.tipoLancamento = ligacao?.statusGrupo!!.substring(7, 8)
        ligacao?.statusTratamentoEsgoto = ligacao?.statusGrupo!!.substring(8, 9)

        ligacao?.economiaResidencial = Integer.parseInt(ligacao?.economia!!.substring(0, 4))
        ligacao?.economiaComercial = Integer.parseInt(ligacao?.economia!!.substring(4, 8))
        ligacao?.economiaIndustrial = Integer.parseInt(ligacao?.economia!!.substring(8, 12))
        ligacao?.economiaPublica = Integer.parseInt(ligacao?.economia!!.substring(12, 16))
        ligacao?.economiaOutras = Integer.parseInt(ligacao?.economia!!.substring(16, 20))
        ligacao?.totalEconomias = (ligacao?.economiaResidencial
                + ligacao?.economiaComercial
                + ligacao?.economiaIndustrial + ligacao?.economiaPublica
                + ligacao?.economiaOutras)

        ligacao?.codigoArrecadacao = ligacao?.codigo!!.substring(0, 3)
        ligacao?.numeroAgencia = ligacao?.codigo!!.substring(3, 7)
        ligacao?.contaCorrente = ligacao?.codigo!!.substring(7, 21)
        ligacao?.codigoRemessa = Integer.parseInt(ligacao?.codigo!!.substring(21, 22))
        ligacao?.codigoCobranca = Integer.parseInt(ligacao?.codigo!!.substring(22, 23))
        ligacao?.codigoCategoria = ligacao?.codigo!!.substring(23, 25)
        ligacao?.codigoFaturamento = Integer.parseInt(ligacao?.codigo!!.substring(25, 26))
        ligacao?.codigoTributo = Integer.parseInt(ligacao?.codigo!!.substring(26, 29))
        ligacao?.criterioFaturamento = Integer.parseInt(ligacao?.codigo!!.substring(29, 31))



        ligacao?.criterioExcecao = Integer.parseInt(ligacao?.dscExce!!.substring(0, 2))
        ligacao?.volumeLimite = Integer.parseInt(ligacao?.dscExce!!.substring(2, 9))
        ligacao?.percentualEsgoto = Integer.parseInt(ligacao?.dscExce!!.substring(9, 15))

        ligacao?.numeroAviso = java.lang.Long.parseLong(ligacao?.numAvis!!.substring(0, 9))
        ligacao?.anoLancamento = Integer.parseInt(ligacao?.numAvis!!.substring(9, 13))
        ligacao?.numeroEmissao = Integer.parseInt(ligacao?.numAvis!!.substring(13, 15))

    }


    /**
     * Define as tarifas.
     */
    @Throws(Exception::class)
    fun apuraMinimo() {

        if (ligacao!!.statusEconomia == "N") {
            ligacao!!.totalEconomias = 1
        }

        if (ligacao!!.economiaResidencial > 0) {

            this.minimoTarifaResidencial = dataBase!!.faixaDao().getLimiteTarifa("R")

            if (ligacao!!.statusEconomia == "S") {
                this.minimoTarifaResidencial *= ligacao!!.economiaResidencial
            }
        }

        if (ligacao!!.economiaComercial > 0) {

            this.minimoTarifaComercial = dataBase!!.faixaDao().getLimiteTarifa("C")

            if (ligacao!!.statusEconomia == "S") {
                this.minimoTarifaComercial *= ligacao!!.economiaComercial
            }
        }

        if (ligacao!!.economiaIndustrial > 0) {

            this.minimoTarifaIndustrial = dataBase!!.faixaDao().getLimiteTarifa("I")

            if (ligacao!!.statusEconomia == "S") {
                this.minimoTarifaIndustrial *= ligacao!!.economiaIndustrial
            }
        }

        if (ligacao!!.economiaPublica > 0) {

            this.minimoTarifaPublica = dataBase!!.faixaDao().getLimiteTarifa("P")

            if (ligacao!!.statusEconomia == "S") {
                this.minimoTarifaPublica *= ligacao!!.economiaPublica
            }
        }

        if (ligacao!!.economiaOutras > 0) {

            this.minimoTarifaOutras = dataBase!!.faixaDao().getLimiteTarifa("O")

            if (ligacao!!.statusEconomia == "S") {
                this.minimoTarifaOutras *= ligacao!!.economiaOutras
            }
        }

        this.minimoTarifa = (this.minimoTarifaResidencial
                + this.minimoTarifaComercial + this.minimoTarifaIndustrial
                + this.minimoTarifaPublica + this.minimoTarifaOutras)
    }

    /**
     * Define tipos de consumo como água e esgoto
     */
    @Throws(Exception::class)
    fun apuraMedido() {

        // inicializa as variáveis que serão
        // utilizadas durante o processo
        this.consumoMedido = 0 // nConsMedido
        this.consumoMedidoEsgoto = 0 // nConsMedidoEE
        this.consumoFaturadoAgua = 0 // nConsFaturadoA
        this.consumoFaturadoEsgoto = 0 // nConsFaturadoEE
        this.creditoConsumo = 0 // nCreConsumo
        this.statusVirada = "N" // sStaVirada

        // Ligação Nova => Leitura Anterior = Leitura de Instalação
        if (ligacao!!.statusLigacaoNova == "S") {
            this.leituraAnterior = ligacao!!.leituraInstalacao
            ligacao!!.dataLeituraAnterior = ligacao!!.dataInstalacao
        }

        // Leitura não realizada
        if (this.codigoLeituraInterno == COD_LEIT_INTERNO_907 && this.leituraAtual == ligacao!!.leituraAnteriorInt) {
            this.leituraAtual = ligacao!!.leituraAnteriorInt
            this.leituraAnterior = ligacao!!.leituraAnteriorInt
        }

        /** codigo_leitura é ocorrência (sOcorr1) confere para ver se existe
         * código de ocorrência e não houve leitura
         */

        if (this.codigoLeitura > 0 && this.leituraAtual == 0 && this.quantidadeTentativas == 0) {
            if (ligacao!!.statusTroca == "S") {
                this.leituraAtual = ligacao!!.leituraInstalacao
            } else {
                this.leituraAtual = ligacao!!.leituraAnteriorInt
            }
        }

        // Se a data anterior não for nula
        if (ligacao!!.dataLeituraAnterior != ""
                && ligacao!!.dataLeituraAnterior != "01/01/1900"
                && ligacao!!.dataLeituraAnterior != "00/00/0000") {

            val dAtual = this.dataLeitura

            val calculoData: Int
            val sdf = SimpleDateFormat("dd/mm/yyyy")
            sdf.parse(ligacao!!.dataLeituraAnterior)
            val c1 = Calendar.getInstance()
            val c2 = Calendar.getInstance()
            c1.time = sdf.parse(ligacao!!.dataLeituraAnterior)
            c2.time = dAtual!!
            //  val BRAZIL =   DateTimeZone.forID(DateTimeZone.getDefault().id)

            // val start = DateTime(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH) +1 , c1.get(Calendar.DAY_OF_MONTH), 0, 0, 0, BRAZIL)
            // val end = DateTime(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH) +1, c2.get(Calendar.DAY_OF_MONTH), 0, 0, 0, BRAZIL)

            calculoData =  c2.time.time.minus(c1.time.time).toInt()   /// Days.daysBetween(start, end).days

            if (calculoData > 99) {
                this.quantidadeDiasConsumo = 99
            } else if (calculoData < 0) {
                this.quantidadeDiasConsumo = 30
            } else {
                this.quantidadeDiasConsumo = calculoData
            }
        } else {
            this.quantidadeDiasConsumo = 30
        }


        // Agua/Agua e Esgoto
        if (ligacao!!.codigoFaturamento == 1 || ligacao!!.codigoFaturamento == 2) {
            this.consumoMedido = this.leituraAtual - ligacao!!.leituraAnteriorInt
            this.consumoMedidoEsgoto = 0
        } else if (ligacao!!.codigoFaturamento == 4 || ligacao!!.codigoFaturamento == 5) {
            this.consumoMedido = this.leituraAtual - ligacao!!.leituraAnteriorInt
            this.consumoMedidoEsgoto = this.leituraAtual - ligacao!!.leituraAnteriorInt
        } else {
            this.consumoMedidoEsgoto = this.leituraAtual - ligacao!!.leituraAnteriorInt
            this.consumoMedido = 0
        }// TEE
        // Agua/Esgoto/TEE


        if (this.leituraAtual < ligacao!!.leituraAnteriorInt) {
            this.calculaVirada()
        }

        if (this.consumoMedido < 0 || ligacao!!.tipoLigacao == 0) {
            this.consumoMedido = 0
        }

        if (this.consumoMedidoEsgoto < 0 || ligacao!!.codigoFaturamento != 3) {
            this.consumoMedidoEsgoto = 0
        }

    }

    /* *
        * Verifica troca
        **/


    fun verificaTroca() {

        if (ligacao!!.statusTroca == "S") {

            this.consumoMedido = this.leituraAtual - ligacao!!.leituraInstalacao + ligacao!!.consumoResidual

            if (this.consumoMedido < 0) {
                this.consumoMedido = 0
            }

            // esgoto especial medido
            if (ligacao!!.tipoLigacao > 0 && ligacao!!.codigoFaturamento == 3) {
                this.consumoMedidoEsgoto = this.consumoMedido
                this.consumoMedido = 0
            }
        }
    }

    /*
        * Apura Novecentos
        * */
    @Throws(Exception::class)
    fun apura900() {

        var statusAceita: String? = null
        var repasseInterno: String? = null
        var statusCriterio: String? = null
        var ocorrencia: Ocorrencia? = null
        val soma = this.consumoMedido + this.consumoMedidoEsgoto

        ocorrencia = dataBase!!.ocorrenciaDao().buscarOcorrencia(this.codigoLeitura)

        // Verifica os parâmetros do Código de Leitura digitado
        if (ocorrencia != null) {
            statusAceita = ocorrencia.statusAceita
            this.statusRepasse = ocorrencia.statusRepasse
            statusCriterio = ocorrencia.statusCriterio
            this.statusEmissao = ocorrencia.statusEmissao
        } else {
            this.statusErro = 54
            statusAceita = "N"
            this.statusRepasse = "N"
            statusCriterio = "N"
            this.statusEmissao = "N"
        }

        // Leitura não efetuada
        if (this.codigoLeituraInterno == COD_LEIT_INTERNO_907 && this.leituraAtual == ligacao!!.leituraAnteriorInt) {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_907
        }

        // Codigo de Ocorrencia que não aceita leitura
        if (this.codigoLeitura > 0 && statusAceita == "N") {
            this.codigoLeituraInterno = this.codigoLeitura // não enquadra nos
            // 900

            if (ligacao!!.statusTroca == "S") {
                // Adotar a Leitura de Instalacao do novo medidor
                this.leituraAtual = ligacao!!.leituraInstalacao
            } else {
                // Igualar a leitura de acordo com a última
                this.leituraAtual = ligacao!!.leituraAnteriorInt
            }

            this.consumoMedido = 0
            this.consumoMedidoEsgoto = 0

            // Codigo de Ocorrencia que aceita leitura
            // Leitura igual a zero ou a anterior para antes e depois do calculo
            // Não foi digitada nenhuma leitura
        } else if (this.codigoLeitura > 0
                && (this.leituraAtual == 0 || this.leituraAtual == ligacao!!.leituraAnteriorInt)
                && this.quantidadeTentativas == 0) {

            this.codigoLeituraInterno = this.codigoLeitura // nao enquadra nos
            // 900
            this.leituraAtual = ligacao!!.leituraAnteriorInt

            /*// Pedido 5868 - Alessandra
                               CODIGO DE OCORRENCIA QUE NAO ACEITA LEITURA

                               FORCA O CRITERIO

                                NÃO ENQUADRA NOS 900 */

        } else if (this.codigoLeitura > 0 && statusCriterio == "S") {

            this.codigoLeituraInterno = this.codigoLeitura


            // leitura atual == anterior
        } else if (this.leituraAtual == ligacao!!.leituraAnteriorInt) {

            this.codigoLeituraInterno = COD_LEIT_INTERNO_905

            // Leitura Anterior > Atual e não foi troca e não foi virada de
            // hidrômetro
        } else if (ligacao!!.leituraAnteriorInt > this.leituraAtual
                && ligacao!!.statusTroca == "N"
                && this.statusVirada == "N") {

            this.codigoLeituraInterno = COD_LEIT_INTERNO_906

            // Consumo abaixo do mínimo
        } else if (soma < ligacao!!.consumoMinimo / ligacao!!.totalEconomias) {

            // Média do Medido Superior ao Mínimo
            if (ligacao!!.consumoMedio > ligacao!!.consumoMinimo) {
                this.codigoLeituraInterno = COD_LEIT_INTERNO_908
                // Normal - Perfil de Consumo Abaixo da Mínima - Não enquadrar
                // 908
            } else {
                this.codigoLeituraInterno = COD_LEIT_INTERNO_900
            }

            // Acima da média e maior que a máxima B
        } else if (soma > ligacao!!.consumoMaximoA && soma <= ligacao!!.consumoMaximoB) {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_901

            // acima da máxima B
        } else if (soma > ligacao!!.consumoMaximoB) {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_902

            // abaixo da média e maior que a mínima B
        } else if (soma < ligacao!!.consumoMinimoA && soma >= ligacao!!.consumoMinimoB) {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_903

            // abaixo da mínima
        } else if (soma < ligacao!!.consumoMinimoB) {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_904

            // Normal
        } else {
            this.codigoLeituraInterno = COD_LEIT_INTERNO_900
        }

        ocorrencia = dataBase?.ocorrenciaDao()?.buscarOcorrencia(this.codigoLeituraInterno)

        if (ocorrencia != null) {
            repasseInterno = ocorrencia.statusRepasse
            this.statusEmissao = ocorrencia.statusEmissao
        } else {
            repasseInterno = "N"
            this.statusEmissao = "N"
            this.statusErro = 54
        }

        if (this.statusRepasse == "S" || repasseInterno == "S") {
            this.statusRepasse = "S"
        }

        // caso haja troca com critério definido
        // mantém o critério da troca
        if (ligacao!!.statusTroca == "N" || ligacao!!.statusTroca == "S" && this.criterioFaturamento == 10) {

            // Adota o critério do enquadramento de leitura
            if (this.codigoLeituraInterno != codigoLeitura) {
                ocorrencia = dataBase?.ocorrenciaDao()?.buscarOcorrencia(
                        this.codigoLeituraInterno)
            } else {
                ocorrencia = dataBase?.ocorrenciaDao()?.buscarOcorrencia(
                        this.codigoLeitura)
            }

            if (ocorrencia != null) {
                try {
                    this.criterioFaturamento = Integer.parseInt(ocorrencia.criterioFaturamento)
                } catch (e: Exception) {

                }

            } else {
                this.statusErro = 54
                this.criterioFaturamento = 10
            }
        }
    }

    /**
     * Apura Exceção
     *
     */

    fun apuraExcecao() {

        // Agua / Agua e Esgoto / TEE
        if (ligacao!!.codigoFaturamento == 3
                || ligacao!!.codigoFaturamento == 4
                || ligacao!!.codigoFaturamento == 5) {

            this.consumoFaturadoEsgoto = ligacao!!.consumoFaturadoEsgotoEspecial!!
            this.excecaoConsumoFaturadoEsgoto = this.consumoFaturadoEsgoto
        } else {
            this.consumoFaturadoEsgoto = 0
            this.excecaoConsumoFaturadoEsgoto = 0
        }
    }

    /**
     * Apura Pipa
     */


    fun apuraPipa() {}

    /**
     * Apura Estimado
     */
    fun apuraEstimado() {

        val faturadoDividido = 0.0
        val residuo = 0.0
        var minimoEconomiaResidencial = 1.0
        var minimoEconomiaComercial = 1.0
        var minimoEconomiaIndustrial = 1.0
        var minimoEconomiaPublica = 1.0
        var minimoEconomiaOutras = 1.0

        this.consumoFaturadoAgua = 0
        this.consumoFaturadoEsgoto = 0

        if (ligacao!!.economiaResidencial > 0) {
            minimoEconomiaResidencial = (this.minimoTarifaResidencial / ligacao!!.economiaResidencial).toDouble()
        }

        if (ligacao!!.economiaComercial > 0) {
            minimoEconomiaComercial = (this.minimoTarifaComercial / ligacao!!.economiaComercial).toDouble()
        }

        if (ligacao!!.economiaIndustrial > 0) {
            minimoEconomiaIndustrial = (this.minimoTarifaIndustrial / ligacao!!.economiaIndustrial).toDouble()
        }

        if (ligacao!!.economiaPublica > 0) {
            minimoEconomiaPublica = (this.minimoTarifaPublica / ligacao!!.economiaPublica).toDouble()
        }

        if (ligacao!!.economiaOutras > 0) {
            minimoEconomiaOutras = (this.minimoTarifaOutras / ligacao!!.economiaOutras).toDouble()
        }

        // Ligacao Hidrometrada / Agua ou Agua e Esgoto
        if (ligacao!!.tipoLigacao > 0 && ligacao!!.codigoFaturamento != 3) {
            this.consumoFaturadoAgua = this.consumoMedido + ligacao!!.consumoPipa
        }

        // Água / Agua e Esgoto
        if (ligacao!!.codigoFaturamento == 1 || ligacao!!.codigoFaturamento == 2) {

            this.consumoFaturadoAgua = ligacao!!.consumoEstimado

            if (ligacao!!.consumoMinimo > this.consumoFaturadoAgua) {
                this.consumoFaturadoAgua = ligacao!!.consumoMinimo
            }

            // Água / Esgoto / TEE
        } else if (ligacao!!.codigoFaturamento == 4 || ligacao!!.codigoFaturamento == 5) {

            this.consumoFaturadoAgua = ligacao!!.consumoEstimado

            if (ligacao!!.consumoMinimo > this.consumoFaturadoAgua) {
                this.consumoFaturadoAgua = ligacao!!.consumoMinimo
            }

            this.consumoFaturadoEsgoto = ligacao!!.percentualEsgoto

            if (ligacao!!.consumoMinimo > this.consumoFaturadoEsgoto) {
                this.consumoFaturadoEsgoto = ligacao!!.consumoMinimo
            }

            // TEE
        } else {

            this.consumoFaturadoEsgoto = ligacao!!.consumoEstimado

            if (ligacao!!.consumoMinimo > this.consumoFaturadoEsgoto) {
                this.consumoFaturadoEsgoto = ligacao!!.consumoMinimo
            }
        }

        ligacao!!.consumoPipa = ligacao!!.consumoPipa - this.consumoFaturadoAgua
        ligacao!!.consumoPipa = if (ligacao!!.consumoPipa < 0)
            0
        else
            ligacao!!.consumoPipa

        // Exceção
        if (this.excecaoConsumoFaturadoEsgoto > 0 && ligacao!!.codigoFaturamento > 2) {
            this.consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto
        }

        this.consumoMedido = 0
        this.consumoFaturadoAgua = this.consumoFaturadoAgua + ligacao!!.consumoPipa

    }

    /*
        * Confere se o hidrômetro virou - chegou no limite dos dígitos e começou do
        * zero novamente
        * */


    fun calculaVirada() {

        val viradaMaxima: Int
        val consumoVirada: Int

        // 5% de tolerância além do máximo
        viradaMaxima = (ligacao!!.consumoMaximoB + ligacao!!.leituraMaxima * 0.05).toInt()
        consumoVirada = ligacao!!.leituraMaxima - ligacao!!.leituraAnteriorInt + this.leituraAtual

        // suspeita de virada de hidrômetro
        if (consumoVirada < viradaMaxima) {

            statusVirada = "S"

            if (ligacao!!.codigoFaturamento == 1 || ligacao!!.codigoFaturamento == 2) {
                // água e esgoto
                this.consumoMedido = consumoVirada
                this.consumoMedidoEsgoto = 0
            } else if (ligacao!!.codigoFaturamento == 4 || ligacao!!.codigoFaturamento == 5) {
                // água/esgoto/TEE
                this.consumoMedido = consumoVirada
                this.consumoMedidoEsgoto = consumoVirada
            } else {
                // TEE
                this.consumoMedido = 0
                this.consumoMedidoEsgoto = consumoVirada
            }

            // caso não seja, zera o medidor
            // para não ficar negativo
        } else {
            statusVirada = "N"
            this.consumoMedido = 0
            this.consumoMedidoEsgoto = 0
        }

    }

    /**
     * Apura faturado
     *
     */

    @Throws(Exception::class)
    fun apuraFaturado() {

        val credito_saldo: Boolean
        var saldo = 0
        var consFaturado = 0
        var valor_faturado = 0
        var minimoEconomiaResidencial = 1.0
        var minimoEconomiaComercial = 1.0
        var minimoEconomiaIndustrial = 1.0
        var minimoEconomiaPublica = 1.0
        var minimoEconomiaOutras = 1.0
        var soma = false
        val normal = 0

        this.creditoConsumo = 0

        var credNegativoAtual = 0
        var consFat = 0.0

        if (ligacao!!.economiaResidencial > 0) {
            minimoEconomiaResidencial = this.minimoTarifaResidencial.toDouble() / ligacao!!.economiaResidencial
        }

        if (ligacao!!.economiaComercial > 0) {
            minimoEconomiaComercial = this.minimoTarifaComercial.toDouble() / ligacao!!.economiaComercial
        }

        if (ligacao!!.economiaIndustrial > 0) {
            minimoEconomiaIndustrial = this.minimoTarifaIndustrial.toDouble() / ligacao!!.economiaIndustrial
        }

        if (ligacao!!.economiaPublica > 0) {
            minimoEconomiaPublica = this.minimoTarifaPublica.toDouble() / ligacao!!.economiaPublica
        }

        if (ligacao!!.economiaOutras > 0) {
            minimoEconomiaOutras = this.minimoTarifaOutras.toDouble() / ligacao!!.economiaOutras
        }

        // Ligacao Hidrometrada / Agua ou Agua e Esgoto
        if (ligacao!!.tipoLigacao > 0 && ligacao!!.codigoFaturamento != 3) {

            //Somar consumo medido + consumo caminhao pipa ( caso houver )
            val consumo = (this.consumoMedido + ligacao!!.consumoPipa).toDouble()

            //Ped:8938, calcular o consumo faturado proporcianal a qtd. de dias do mes / Douglas
            // REGRAS:
            //   1 - Consumo medido maior que consumo minimo.
            //   2 - Codigo de leitura = 0-Normal (sem ocorrencia de leitura)
            //   3 - Dias de consumo maior que quantidade de dias da referencia de calculo.
            //   4 - Nao pode existir excecao de faturamento (tpo_excecao = 2-Criterio de Faturamento)


            if (tarefa!!.staFatProporDia == "S"
                    && this.consumoMedido > ligacao!!.consumoMinimo
                    && this.codigoLeitura == normal
                    && this.quantidadeDiasConsumo > tarefa!!.qtdDiasFaturar) {

                //Não pode existir exceção de faturamento
                if (ligacao!!.staExcecaoFat == "N") {

                    consFat = NumberUtils.round(
                            consumo / this.quantidadeDiasConsumo * tarefa!!.qtdDiasFaturar, 0)

                    //Ped:9236, calculo da proporcionalidade é menor que o zero ou
                    //menor que o volume minimo / Douglas 16/04/2013
                    if (consFat < 0 || consFat < ligacao!!.consumoMinimo) {
                        consFat = ligacao!!.consumoMinimo.toDouble()
                    }

                    //Calcular a diferenca do consumo medido para o consumo faturado, enviar para a proxima referencia
                    //Credito negativo, cobrar do cliente no proximo faturamento
                    credNegativoAtual = NumberUtils.round(consFat - consumo, 0).toInt()

                    //Cobrar credito negativo vindo de referencia anterior
                    if (ligacao!!.creditoConsumo!! < 0) {
                        consFat = consFat - ligacao!!.creditoConsumo!!
                        ligacao!!.creditoConsumo = 0
                    }

                    this.consumoFaturadoAgua = consFat.toInt()

                } else {

                    this.consumoFaturadoAgua = consumo.toInt()
                }

            } else {

                this.consumoFaturadoAgua = consumo.toInt()

            }

        }

        when (ligacao!!.codigoFaturamento) {

            // água e esgoto especial
            4 -> {
                this.consumoFaturadoEsgoto = this.consumoMedidoEsgoto
                this.consumoFaturadoAgua = this.consumoMedido
            }

            // Esgoto Especial
            3 -> {
                this.consumoFaturadoAgua = 0
                this.consumoFaturadoEsgoto = this.consumoMedidoEsgoto

                // tem exceção
                if (this.excecaoConsumoFaturadoEsgoto > 0) {
                    this.consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto
                }
            }

            // Água, Esgoto e Esgoto Especial
            5 -> {
                this.consumoFaturadoEsgoto = this.consumoMedidoEsgoto
                this.consumoFaturadoAgua = this.consumoMedido
            }
        }

        // Prevalecer o Critério da Excecao
        if (ligacao!!.criterioExcecao != 10 && ligacao!!.criterioExcecao != 21
                && ligacao!!.criterioExcecao != 51) {

            this.criterioFaturamento = ligacao!!.criterioExcecao
        }

        if (ligacao!!.codigoFaturamento > 2) {

            when (ligacao!!.codigoFaturamento) {

                3 -> {
                    this.consumoFaturadoEsgoto = this.consumoMedidoEsgoto

                    // tem exceção
                    if (this.excecaoConsumoFaturadoEsgoto > 0) {
                        this.consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto
                    }
                }

                4 -> {
                    consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto
                }

                5 -> {
                    this.consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto
                }

                else -> {
                    this.consumoFaturadoEsgoto = this.excecaoConsumoFaturadoEsgoto - this.consumoMedidoEsgoto
                }
            }
        }

        if (ligacao!!.codigoFaturamento != 3) {
            consFaturado = this.consumoFaturadoAgua
        } else {
            consFaturado = this.consumoFaturadoEsgoto
        }

        // Critério normal considerar credito de consumo
        if (this.criterioFaturamento == 10
                || this.criterioFaturamento == 21
                || this.criterioFaturamento == 51) {

            if ((consFaturado >= ligacao!!.consumoMinimo || ligacao!!.creditoConsumo!! < 0) && ligacao!!.creditoConsumo != 0) {

                if (isCalcularCredito) {

                    if (consFaturado >= ligacao!!.creditoConsumo!!) {
                        credito_saldo = true // não há saldo de crédito
                    } else {
                        credito_saldo = false
                        valor_faturado = consFaturado
                    }

                    // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                    if (ligacao!!.creditoConsumo!! < 0 && consFaturado < ligacao!!.consumoMinimo) {
                        consFaturado = ligacao!!.consumoMinimo
                    }

                    // desconto de crédito
                    consFaturado = consFaturado - ligacao!!.creditoConsumo!!

                    // Se não der para descontar, acumula no saldo
                    if (consFaturado < ligacao!!.consumoMinimo
                            && ligacao!!.creditoConsumo!! > 0
                            && credito_saldo == false) {
                        if (valor_faturado < ligacao!!.creditoConsumo!!) {
                            saldo = ligacao!!.creditoConsumo!! - valor_faturado
                        } else {
                            saldo = ligacao!!.creditoConsumo!! + consFaturado
                        }
                    } else {
                        saldo = 0
                    }

                } else {
                    saldo = ligacao!!.creditoConsumo!!
                }

            } else {
                saldo = ligacao!!.creditoConsumo!!
            }

            // Critério Condicional
            // (Consumo > Media Medido) / Critério da Excecao(descontar credito)
        } else if ((this.criterioFaturamento == 19 || this.criterioFaturamento == 20) && (ligacao!!.criterioExcecao == 19 || ligacao!!.criterioExcecao == 20)) {

            if (this.consumoMedido > ligacao!!.consumoMedio) {

                // Adotar a média do medido
                if (this.criterioFaturamento == 19) {

                    if (ligacao!!.consumoMedio >= ligacao!!.creditoConsumo!!) {
                        // não há saldo de crédito
                        credito_saldo = true
                    } else {
                        valor_faturado = ligacao!!.consumoMedio
                        credito_saldo = false
                    }

                    // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                    if (ligacao!!.creditoConsumo!! < 0 && ligacao!!.consumoMedio < ligacao!!.consumoMinimo) {
                        ligacao!!.consumoMedio = ligacao!!.consumoMinimo
                    }

                    // desconto de crédito
                    consFaturado = ligacao!!.consumoMedio - ligacao!!.creditoConsumo!!

                } else {

                    if (ligacao!!.consumoMedioFaturado >= ligacao!!.creditoConsumo!!) {
                        // não há saldo de crédito
                        credito_saldo = true
                    } else {
                        credito_saldo = false
                        valor_faturado = ligacao!!.consumoMedioFaturado
                    }

                    // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                    if (ligacao!!.creditoConsumo!! < 0 && ligacao!!.consumoMedioFaturado < ligacao!!.consumoMinimo) {
                        ligacao!!.consumoMedioFaturado = ligacao!!.consumoMinimo
                    }

                    // desconto de crédito
                    consFaturado = ligacao!!.consumoMedioFaturado - ligacao!!.consumoMinimo
                }

                if (consFaturado < ligacao!!.consumoMinimo
                        && ligacao!!.creditoConsumo!! > 0
                        && credito_saldo == false) {
                    if (valor_faturado < ligacao!!.creditoConsumo!!) {
                        saldo = ligacao!!.creditoConsumo!! - valor_faturado
                    } else {
                        saldo = ligacao!!.creditoConsumo!! + consFaturado
                    }
                } else {
                    saldo = ligacao!!.creditoConsumo!!
                }

                // adotar o critério normal
            } else {

                if ((consFaturado >= ligacao!!.consumoMinimo || ligacao!!.creditoConsumo!! < 0) && ligacao!!.creditoConsumo != 0) {

                    if (isCalcularCredito) {

                        if (consFaturado >= ligacao!!.creditoConsumo!!) {
                            // não há saldo de crédito
                            credito_saldo = true
                        } else {
                            credito_saldo = true
                            valor_faturado = consFaturado
                        }

                        // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                        if (ligacao!!.creditoConsumo!! < 0 && consFaturado < ligacao!!.consumoMinimo) {
                            consFaturado = ligacao!!.consumoMinimo
                        }

                        // desconto de crédito
                        consFaturado = consFaturado - ligacao!!.creditoConsumo!!

                        // Se não der para descontar, acumula no saldo
                        if (consFaturado < ligacao!!.consumoMinimo
                                && ligacao!!.creditoConsumo!! > 0
                                && credito_saldo == false) {

                            if (valor_faturado < ligacao!!.creditoConsumo!!) {
                                saldo = ligacao!!.creditoConsumo!! - valor_faturado
                            } else {
                                saldo = ligacao!!.creditoConsumo!! + consFaturado
                            }

                        } else {
                            saldo = 0
                        }

                    } else {
                        saldo = ligacao!!.creditoConsumo!!
                    }

                } else {
                    saldo = ligacao!!.creditoConsumo!!
                }


            }

            // Critério Condicional
            // (consumo > media medido) / Critério de Codigo(nao descontar
            // credito)
        } else if ((this.criterioFaturamento == 19 || this.criterioFaturamento == 20) && ligacao!!.criterioExcecao != 19 && ligacao!!.criterioExcecao != 20) {

            if (this.consumoMedido > ligacao!!.consumoMedio) {

                if (ligacao!!.creditoConsumo!! >= 0) {

                    // Adotar a média do medido
                    if (this.criterioFaturamento == 19) {
                        consFaturado = ligacao!!.consumoMedio
                    } else {
                        consFaturado = ligacao!!.consumoMedioFaturado
                    }

                    saldo = ligacao!!.creditoConsumo!!

                } else {

                    // Adotar a média do medido + credito negativo
                    if (this.criterioFaturamento == 19) {
                        // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                        if (ligacao!!.consumoMedio < ligacao!!.consumoMinimo) {
                            ligacao!!.consumoMedio = ligacao!!.consumoMinimo
                        }
                        consFaturado = ligacao!!.consumoMedio - ligacao!!.creditoConsumo!!
                    } else {
                        // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                        if (ligacao!!.consumoMedioFaturado < ligacao!!.consumoMinimo) {
                            ligacao!!.consumoMedioFaturado = ligacao!!.consumoMinimo
                        }
                        // Adotar a média do faturado + credito negativo
                        consFaturado = ligacao!!.consumoMedioFaturado - ligacao!!.creditoConsumo!!
                    }
                }

                soma = true

                // ADOTAR O CRITÉRIO NORMAL
            } else {

                if ((consFaturado >= ligacao!!.consumoMinimo || ligacao!!.creditoConsumo!! < 0) && ligacao!!.creditoConsumo != 0) {

                    if (isCalcularCredito) {

                        if (consFaturado >= ligacao!!.creditoConsumo!!) {
                            // nao há saldo de crédito
                            credito_saldo = true
                        } else {
                            valor_faturado = consFaturado
                            credito_saldo = false
                        }

                        // Ped:9236 - Quando há credito negativo, adotar o volume minimo antes de cobrar o crédito / Douglas 16/04/2013
                        if (ligacao!!.creditoConsumo!! < 0 && consFaturado < ligacao!!.consumoMinimo) {
                            consFaturado = ligacao!!.consumoMinimo
                        }

                        // desconto de crédito
                        consFaturado = consFaturado - ligacao!!.creditoConsumo!!
                        soma = false

                        // Se não der para descontar, acumula no saldo
                        if (consFaturado < ligacao!!.consumoMinimo
                                && ligacao!!.creditoConsumo!! > 0
                                && credito_saldo == false) {

                            if (valor_faturado < ligacao!!.creditoConsumo!!) {
                                saldo = ligacao!!.creditoConsumo!! - valor_faturado
                            } else {
                                saldo = ligacao!!.creditoConsumo!! + consFaturado
                            }

                        } else {
                            saldo = 0
                        }

                    } else {
                        saldo = ligacao!!.creditoConsumo!!
                    }

                } else {
                    saldo = ligacao!!.creditoConsumo!!
                }
            }

            // CRITÉRIO DE FATURAMENTO ANORMAL DEFINIDO
        } else {

            /* Ped: 9240 - Aplicar o credito NEGATIVO da referencia anterior
                        * caso tenha criterio de faturamento definido como anormal (cód. leitura impeditivo) - Douglas - 06/06/2013
                        */

            if (ligacao!!.creditoConsumo!! > 0) {
                saldo = ligacao!!.creditoConsumo!!
            }

            when (this.criterioFaturamento) {

                // Media Medido sem/com Credito (11,12)
                11 -> {
                    consFaturado = ligacao!!.consumoMedio
                    soma = true
                }

                12 -> {
                    consFaturado = ligacao!!.consumoMedio
                    soma = true
                    // com crédito
                    this.creditoConsumo = ligacao!!.consumoMedio
                }

                // Media Faturado sem/com Credito (13,14)
                13 -> {
                    consFaturado = ligacao!!.consumoMedioFaturado
                    soma = true
                }

                14 -> {
                    consFaturado = ligacao!!.consumoMedioFaturado
                    soma = true
                    // com crédito
                    this.creditoConsumo = ligacao!!.consumoMedioFaturado
                    if (ligacao!!.situacaoLigacao == "C" || ligacao!!.situacaoLigacao == "K") {
                        this.criterioFaturamento = 13
                    }
                }

                // Mínimo sem/com Crédito (15,16)
                15 -> {
                    consFaturado = ligacao!!.consumoMinimo
                    soma = true
                }

                16 -> {
                    consFaturado = ligacao!!.consumoMinimo
                    soma = true
                    // com crédito
                    this.creditoConsumo = ligacao!!.consumoMinimo
                    if (ligacao!!.situacaoLigacao == "C" || ligacao!!.situacaoLigacao == "K") {
                        this.criterioFaturamento = 15
                    }
                }

                // Faixa B sem/com Crédito (17,18)
                17 -> {
                    consFaturado = ligacao!!.consumoMaximoB
                    soma = true
                }

                18 -> {
                    consFaturado = ligacao!!.consumoMaximoB
                    soma = true
                    // com crédito
                    this.creditoConsumo = ligacao!!.consumoMaximoB
                    if (ligacao!!.situacaoLigacao == "C" || ligacao!!.situacaoLigacao == "K") {
                        this.criterioFaturamento = 17
                    }
                }

                // Faturar até o limite
                52 -> {

                    if (consFaturado > ligacao!!.volumeLimite) {
                        consFaturado = ligacao!!.volumeLimite
                    }
                }

                // Isento até o limite
                53 -> {
                    if (consFaturado > ligacao!!.volumeLimite) {
                        consFaturado = consFaturado - ligacao!!.volumeLimite
                    } else {
                        consFaturado = 0
                    }
                }

                // Faturar o limite
                54 -> {
                    consFaturado = ligacao!!.volumeLimite
                }
            }

            // Ped: 9240 - Douglas - 06/06/2013
            // HÁ CREDITO NEGATIVO ANTERIOR E NÃO HÁ EXCEÇÃO DE FATURAMENTO
            if (ligacao!!.creditoConsumo!! < 0 && this.criterioFaturamento != 50
                    && this.criterioFaturamento != 51
                    && this.criterioFaturamento != 52
                    && this.criterioFaturamento != 53
                    && this.criterioFaturamento != 54) {
                consFaturado = consFaturado - ligacao!!.creditoConsumo!!
            } else {
                saldo = ligacao!!.creditoConsumo!!
            }

        }

        // só esgoto
        if (ligacao!!.codigoFaturamento == 3) {
            this.consumoFaturadoAgua = 0
        } else {
            this.consumoFaturadoAgua = consFaturado
            if (ligacao!!.codigoFaturamento == 1 || ligacao!!.codigoFaturamento == 2) {
                this.consumoFaturadoEsgoto = 0
            }
        }

        // Realizar leituras nas ligações de corte
        if (ligacao!!.situacaoLigacao == "C" || ligacao!!.situacaoLigacao == "K") {

            if (this.consumoMedido > 0) {
                this.creditoConsumo = 0
            }

            if (this.consumoMedido == 0) {
                this.creditoConsumo = ligacao!!.creditoConsumo!!
            }
        }

        //Ped:8938, forçar a soma do saldo quando há credito negativo
        if (saldo < 0) {
            soma = true
        }

        if (ligacao!!.situacaoLigacao == "A" && soma == true) {
            this.creditoConsumo = this.creditoConsumo + saldo
        }

        //Ped:8938, considerar credito atual negativo.
        if (credNegativoAtual != 0) {
            this.creditoConsumo = this.creditoConsumo + credNegativoAtual
        }

        if (ligacao!!.tipoLigacao == 0) {
            ligacao!!.consumoPipa = ligacao!!.consumoPipa - this.consumoFaturadoAgua
            if (ligacao!!.consumoPipa < 0) {
                ligacao!!.consumoPipa = 0
            }

            this.consumoFaturadoAgua = this.consumoFaturadoAgua + ligacao!!.consumoPipa

        }

        // Adotar o mínimo quando o faturado for inferior
        if (ligacao!!.codigoFaturamento != 3
                && this.consumoFaturadoAgua < ligacao!!.consumoMinimo
                && this.criterioFaturamento != 50) {
            this.consumoFaturadoAgua = ligacao!!.consumoMinimo
        }

        if ((ligacao!!.codigoFaturamento == 3
                        || ligacao!!.codigoFaturamento == 4 || ligacao!!.codigoFaturamento == 5)
                && this.consumoFaturadoEsgoto < ligacao!!.consumoMinimo
                && this.criterioFaturamento != 50) {
            this.consumoFaturadoEsgoto = ligacao!!.consumoMinimo
        }

    }

    // Gera Itens
    @Throws(Exception::class)
    fun geraItens() {

        var imposto = 0.0
        var impostoBase = 0.0
        var desconto = 0.0
        var valorBaseImposto = 0.0
        val status = "I"

        val parametros = this.tarefa

        this.apuraValorMinimo("N")

        //Verifica se existe tratamento de esgoto com proporcionalidade

        this.statusTratEsgotoProporcional = parametros?.statusTratEsgotoProporcional

        if (ligacao!!.statusTratamentoEsgoto == "S" && this.statusTratEsgotoProporcional == "S") {
            this.apuraDiasTratEsgotoProporcional(parametros?.dataTratEsgotoProporcional)
        } else {
            this.quantidadeDiasTratEsgoto = 0
        }

        this.calculaTarifas()

        // Realizar leituras nas ligações de corte a pedido
        if (this.valorAgua > 0
                || ligacao!!.situacaoLigacao == "C"
                || ligacao!!.situacaoLigacao == "K") {

            // Desconto Promocional
            // Existe Rubrica de Desconto
            if (parametros?.rubricaDesconto!! > 0
                    && parametros.aliquotaDesconto > 0
                    && NumberUtils.compare(this.valorAgua - this.valorAgua * parametros.aliquotaDesconto,
                            this.minimoAgua) != -1) {

                // Acima do mínimo da Água
                desconto = this.valorAgua * parametros.aliquotaDesconto * -1
            }

            // Atualiza o valor para o mínimo da Água
            if (NumberUtils.compare(this.valorAgua, this.minimoAgua) == -1) {
                this.valorAgua = this.minimoAgua
            }

            if (ligacao!!.tipoImposto == status) {
                impostoBase = 0.0
                imposto = 0.0
            } else {
                impostoBase = this.valorAgua * parametros.fatorReducaoAgua
                imposto = impostoBase * parametros.aliquotaAgua
            }

            // Isento de Água e Esgoto ou Consumo Faturado Zerado
            if (ligacao!!.criterioExcecao == 50 || this.consumoFaturadoAgua == 0) {
                this.valorAgua = 0.0
                imposto = 0.0
                impostoBase = 0.0
            }

            valorBaseImposto = if (imposto == 0.0) 0.0 else impostoBase

            if (this.valorAgua > 0
                    || ligacao!!.situacaoLigacao == "C"
                    || ligacao!!.situacaoLigacao == "K") {

                this.gravarRubrica(parametros.rubricaAgua, valorBaseImposto,
                        this.valorAgua, imposto, status)
            }
        }


        // Inclusão de Rubrica de Desconto de Água
        if (parametros?.rubricaDesconto!! > 0) {

            valorBaseImposto = if (parametros.aliquotaAgua == 0.0)
                0.0
            else
                desconto * parametros.fatorReducaoAgua
            imposto = if (parametros.aliquotaAgua == 0.0)
                0.0
            else
                desconto * parametros.aliquotaAgua

            this.gravarRubrica(parametros.rubricaDesconto, valorBaseImposto,
                    desconto, imposto, status)
        }


        // Rubrica de Esgoto
        // pedido 1552 - Realizar leituras de corte a pedido
        if (this.valorEsgoto > 0
                || ligacao!!.situacaoLigacao == "C"
                || ligacao!!.situacaoLigacao == "K") {

            if (NumberUtils.compare(this.valorEsgoto, this.minimoEsgoto) == -1) {
                this.valorEsgoto = this.minimoEsgoto
            }

            impostoBase = this.valorEsgoto * parametros.fatorReducaoEsgoto
            imposto = this.valorEsgoto * parametros.fatorReducaoEsgoto * parametros.aliquotaEsgoto

            if (ligacao!!.tipoImposto == status) {
                impostoBase = 0.0
                imposto = 0.0
            }

            // Isento de Agua e Esgoto / Isento de Esgoto
            if (ligacao!!.criterioExcecao == 50
                    || ligacao!!.criterioExcecao == 51
                    || this.consumoFaturadoAgua == 0) {

                this.valorEsgoto = 0.0
                impostoBase = 0.0
                imposto = 0.0
            }

            if (this.valorEsgoto > 0
                    || ligacao!!.situacaoLigacao == "C"
                    || ligacao!!.situacaoLigacao == "K") {

                valorBaseImposto = if (imposto == 0.0) 0.0 else impostoBase

                this.gravarRubrica(parametros.rubricaEsgoto, valorBaseImposto,
                        this.valorEsgoto, imposto, status)
            }
        }

        // Rubrica de Esgoto Especial
        if (this.valorEsgotoEspecial > 0) {

            if (NumberUtils.compare(this.valorEsgotoEspecial,
                            this.minimoEsgotoEspecial) == -1) {
                this.valorEsgotoEspecial = this.minimoEsgotoEspecial
            }

            impostoBase = this.valorEsgotoEspecial * parametros.fatorReducaoEsgotoEspecial
            imposto = this.valorEsgotoEspecial * parametros.fatorReducaoEsgotoEspecial * parametros.aliquotaEsgotoEspecial

            if (ligacao!!.tipoImposto == status) {
                impostoBase = 0.0
                imposto = 0.0
            }

            valorBaseImposto = if (imposto == 0.0) 0.0 else impostoBase

            this.gravarRubrica(parametros.rubricaEsgotoEspecial,
                    valorBaseImposto, this.valorEsgotoEspecial,
                    imposto, status)
        }

        // Rubrica de Tratamento Esgoto
        // Se o status é "S" é por que tem rubrica de tratamento

        // Realizar leituras nas ligações de corte a pedido
        if (ligacao!!.statusTratamentoEsgoto == "S" && (this.valorTratamentoEsgoto > 0 || ligacao!!.situacaoLigacao == "C"
                        || ligacao!!.situacaoLigacao == "K")) {

            // Não adotar o valor mínimo quando a cobrança for proporcional
            if (NumberUtils.compare(this.valorTratamentoEsgoto,
                            this.minimoTratamento) == -1 && this.statusTratEsgotoProporcional == "N") {
                this.valorTratamentoEsgoto = this.minimoTratamento
            }

            impostoBase = this.valorTratamentoEsgoto * parametros.fatorReducaoTratamento
            imposto = this.valorTratamentoEsgoto * parametros.fatorReducaoTratamento * parametros.aliquotaTratamento

            // Utilização Isenta de Imposto
            if (ligacao!!.tipoImposto == status) {
                impostoBase = 0.0
                imposto = 0.0
            }

            // Isento de Água e Esgoto ou Consumo Faturado Zerado
            if (ligacao!!.criterioExcecao == 50 || this.consumoFaturadoAgua == 0) {
                this.valorTratamentoEsgoto = 0.0
                impostoBase = 0.0
                imposto = 0.0
            }

            // Realizar leituras nas ligações de corte a pedido
            if (this.valorTratamentoEsgoto > 0.0
                    || ligacao!!.situacaoLigacao == "C"
                    || ligacao!!.situacaoLigacao == "K") {

                valorBaseImposto = if (imposto == 0.0) 0.0 else impostoBase

                this.gravarRubrica(parametros.rubricaTratamento,
                        valorBaseImposto, this.valorTratamentoEsgoto, imposto,
                        status)
            }

        } else {

            // Status de tratamento = "S" com valor zerado ou
            // Status de tratamento = "N" com Rubrica cadastrada
            // Insere Rubrica de tratamento de esgoto com valor zerado
            if (ligacao!!.statusTratamentoEsgoto == "S" && this.valorTratamentoEsgoto == 0.0 || ligacao!!.statusTratamentoEsgoto == "N" && parametros.rubricaTratamento > 0) {
                this.valorTratamentoEsgoto = 0.0
                impostoBase = 0.0
                imposto = 0.0
            }

            valorBaseImposto = if (imposto == 0.0) 0.0 else impostoBase

            this.gravarRubrica(parametros.rubricaTratamento, valorBaseImposto,
                    this.valorTratamentoEsgoto, imposto, status)
        }

        // calcula tudo nessa variável
        this.valorTotal = NumberUtils.truncN(this.valorAgua + desconto
                + this.valorEsgoto + this.valorEsgotoEspecial
                + this.valorTratamentoEsgoto, 2)


    }

    private fun resgatarParametros(): Coleta {

        this.tarefa = Coleta()
        val parametroImpressao = dataBase!!.parametroImpressaoDao().getParametroImpressao().blockingFirst()

        val dominios = dataBase?.dominioDao()?.getDominio("DA_CODFATUR", ligacao?.codigoFaturamento.toString())?.blockingFirst()
        dominios.let { this.receita = dominios?.valorDominio + " " + dominios?.descricao }

        if (parametroImpressao != null) {

            var parametro: Parametro? = null;
            // Parâmetros da tabela PARA02
            var parametros =  dataBase?.parametroDao()?.getParametros()?.blockingFirst();
            if (parametros != null && !parametros.isEmpty()) {
                var parametro: Parametro = parametros.get(0)

                tarefa!!.dataInicial = parametro?.dataIni.orEmpty()
                tarefa!!.dataFinal = parametro?.dataFim.orEmpty()
                //   this.nomeTarefa = TarefaDAO.getInstance().carregarParametros("FILE")
            }

            // Parâmetros da tabela PARA02

            // Número Serial do coletor de dados
            //  this.numeroSerial = Settings.romSerialNumber

            // Parâmetros do Cronograma
            tarefa!!.mensagem = parametroImpressao?.msgConta.orEmpty()

            tarefa!!.anoMedicao = parametroImpressao?.refMed?.substring(0, 4)?.toInt()!!
            tarefa!!.mesMedicao = parametroImpressao?.refMed?.substring(4, 6)?.toInt()!!
            tarefa!!.staFatProporDia =  parametroImpressao.StDiaProp.orEmpty()
            tarefa!!.qtdDiasFaturar = parametroImpressao?.qtDiaFatu!!
            tarefa!!.staEmiteBoletoNotif = parametroImpressao?.stCodBar!!
            tarefa!!.valLimiteBoletoNotif = parametroImpressao?.vlCodBar!!
            tarefa!!.valorLimiteSocial = parametroImpressao?.volLimit!!

            tarefa!!.medicao = (NumberUtils.Companion.zeroPad( tarefa!!.mesMedicao, 2)
                    + "/" + tarefa!!.anoMedicao)


            // Quantidade de Serviços da tabela SERV01
            tarefa!!.quantidadeServicos = dataBase?.servicoDao()?.getTotalServicosLeitura()?.blockingFirst()?.size!!

            val codigo = parametroImpressao.cdRubrica

            tarefa!!.rubricaAgua = Integer.valueOf(codigo!!.substring(0, 4))
            tarefa!!.rubricaEsgoto = Integer.valueOf(codigo.substring(4, 8))


            tarefa!!.rubricaEsgotoEspecial = Integer.valueOf(codigo.substring(8, 12))
            tarefa!!.rubricaDesconto = Integer.valueOf(codigo.substring(12, 16))
            tarefa!!.rubricaEnte = Integer.valueOf(codigo.substring(16, 20))
            tarefa!!.rubricaTratamento = Integer.valueOf(codigo.substring(20, 24))

            val fator = parametroImpressao.fatRedu
            tarefa!!.fatorReducaoAgua = java.lang.Double.valueOf(fator!!.substring(0, 8))
            tarefa!!.fatorReducaoEsgoto = java.lang.Double.valueOf(fator.substring(8, 16))
            tarefa!!.fatorReducaoEsgotoEspecial = java.lang.Double.valueOf(fator.substring(16, 24))
            tarefa!!.fatorReducaoTratamento = java.lang.Double.valueOf(fator.substring(24, 32))

            val aliquota = parametroImpressao.descAli
            tarefa!!.aliquotaAgua = java.lang.Double.valueOf(aliquota!!.substring(0, 7))
            tarefa!!.aliquotaEsgoto = java.lang.Double.valueOf(aliquota.substring(7, 14))
            tarefa!!.aliquotaEsgotoEspecial = java.lang.Double.valueOf(aliquota.substring(14, 21))
            tarefa!!.aliquotaDesconto = java.lang.Double.valueOf(aliquota.substring(21, 28))
            tarefa!!.aliquotaEnte = java.lang.Double.valueOf(aliquota.substring(28, 35))
            tarefa!!.aliquotaTratamento = java.lang.Double.valueOf(aliquota.substring(35, 42))

            tarefa!!.statusTratEsgotoProporcional = parametroImpressao.staTrat

            tarefa!!.dataTratEsgotoProporcional = parametroImpressao.dataGer

        }
        return this.tarefa!!
    }

    /*
     *
     * Apura Minimo Categoria
     */
    @Throws(Exception::class)
    fun apuraMinimoCategoria(categoria: String, faturamento: String): Double {

        var tarifa = Tarifa("",
                "",
                0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0)
        this.valorMinimo = 0.0

        tarifa.codigoCategoria = categoria
        tarifa.codigoFaturamento = ligacao!!.codigoFaturamento

        try {
            var tarifas = dataBase?.tarifaDao()?.buscaValorMinimo(categoria, ligacao!!.codigoFaturamento)?.blockingFirst();
            if (tarifas != null && !tarifas.isEmpty() ){
                tarifa = tarifas.get(0)
            }
            if (StringUtils.isNotBlank(tarifa.numReg)) {

                // Água
                if (faturamento == "A") {


                    if (ligacao!!.tipoLigacao == 0) {
                        // Ligação Fixa
                        this.valorMinimo = tarifa.minimoEstimadoAgua
                    } else {
                        // Ligação Hidrometrada
                        this.valorMinimo = tarifa.minimoAgua
                    }

                    // Esgoto
                } else if (faturamento == "E") {

                    // Ligação Fixa
                    if (ligacao!!.tipoLigacao == 0) {

                        // Percentual Fixo de Esgoto
                        if (ligacao!!.percentualEsgoto > 0) {
                            this.valorMinimo = tarifa.minimoEstimadoAgua * ligacao!!.percentualEsgoto
                        } else {
                            this.valorMinimo = tarifa.minimoEstimadoEsgoto
                        }


                        // Ligação Hidrometrada
                    } else {

                        // Percentual Fixo de Esgoto
                        if (ligacao!!.percentualEsgoto > 0) {
                            this.valorMinimo = tarifa.minimoAgua * ligacao!!.percentualEsgoto
                        } else {
                            this.valorMinimo = tarifa.minimoEsgoto
                        }
                    }

                    // Tratamento de esgoto
                } else if (faturamento == "T") {

                    if (ligacao!!.tipoLigacao == 0) {
                        // Ligação Fixa
                        this.valorMinimo = tarifa.minimoEstimadoTratamentoEsgoto
                    } else {
                        // Ligação Hidrometrada
                        this.valorMinimo = tarifa.minimoTratamentoEsgoto
                    }

                }

            } else {
                this.valorMinimo = 0.0
                this.statusErro = 55
            }


        } catch (e: Throwable) {
            Log.d("Erro: ", e.message)
            throw e
            /*  throw new Exception(SystemException.CODE_10, new String[] {
                               Constantes.TABTARIFA, e.getMessage() });*/
        }

        return this.valorMinimo

    }

    /*
        * Apura Valor Mínimo
        */
    @Throws(Exception::class)
    fun apuraValorMinimo(minimo: String) {

        val categoria = this.defineCodigoCategoria()
        var codigoCategoria = ""

        val sigla = arrayOf("A", "E", "T")

        // Água
        if (ligacao!!.codigoFaturamento == 1) {

            if (ligacao!!.economiaResidencial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaResidencial = 1
                }

                codigoCategoria = if (categoria == true) "R1" else "R"

                this.aguaResidencial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaResidencial
            }

            if (ligacao!!.economiaComercial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaComercial = 1
                }

                codigoCategoria = if (categoria == true) "C1" else "C"

                this.aguaComercial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaComercial

            }

            if (ligacao!!.economiaIndustrial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaIndustrial = 1
                }

                codigoCategoria = if (categoria == true) "I1" else "I"

                this.aguaIndustrial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaIndustrial
            }

            if (ligacao!!.economiaPublica > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaPublica = 1
                }

                codigoCategoria = if (categoria == true) "P1" else "P"

                this.aguaPublica = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaPublica

            }

            if (ligacao!!.economiaOutras > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaOutras = 1
                }

                codigoCategoria = if (categoria == true) "O1" else "O"

                this.aguaOutras = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaOutras

            }

            // Água - Esgoto - Esgoto Especial
        } else if (ligacao!!.codigoFaturamento == 2
                || ligacao!!.codigoFaturamento == 4
                || ligacao!!.codigoFaturamento == 5) {


            if (ligacao!!.economiaResidencial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaResidencial = 1
                }

                codigoCategoria = if (categoria == true) "R1" else "R"

                this.aguaResidencial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaResidencial

                this.esgotoResidencial = apuraMinimoCategoria(codigoCategoria,
                        sigla[1]) * ligacao!!.economiaResidencial
            }

            if (ligacao!!.economiaComercial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaComercial = 1
                }

                codigoCategoria = if (categoria == true) "C1" else "C"

                this.aguaComercial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaComercial

                this.esgotoComercial = apuraMinimoCategoria(codigoCategoria,
                        sigla[1]) * ligacao!!.economiaComercial

            }

            if (ligacao!!.economiaIndustrial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaIndustrial = 1
                }

                codigoCategoria = if (categoria == true) "I1" else "I"

                this.aguaIndustrial = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaIndustrial

                this.esgotoIndustrial = apuraMinimoCategoria(codigoCategoria,
                        sigla[1]) * ligacao!!.economiaIndustrial
            }

            if (ligacao!!.economiaPublica > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaPublica = 1
                }

                codigoCategoria = if (categoria == true) "P1" else "P"

                this.aguaPublica = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaPublica

                this.esgotoPublica = apuraMinimoCategoria(codigoCategoria,
                        sigla[1]) * ligacao!!.economiaPublica
            }

            if (ligacao!!.economiaOutras > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaOutras = 1
                }

                codigoCategoria = if (categoria == true) "O1" else "O"

                this.aguaOutras = apuraMinimoCategoria(codigoCategoria,
                        sigla[0]) * ligacao!!.economiaOutras

                this.esgotoOutras = apuraMinimoCategoria(codigoCategoria,
                        sigla[1]) * ligacao!!.economiaOutras

                // Esgoto
            } else if (ligacao!!.codigoFaturamento == 3) {

                if (ligacao!!.economiaResidencial > 0) {

                    if (ligacao!!.statusEconomia == "N") {
                        ligacao!!.economiaResidencial = 1
                    }

                    codigoCategoria = if (categoria == true) "R1" else "R"

                    this.esgotoResidencial = apuraMinimoCategoria(
                            codigoCategoria, sigla[1]) * ligacao!!.economiaResidencial
                }

                if (ligacao!!.economiaComercial > 0) {

                    if (ligacao!!.statusEconomia == "N") {
                        ligacao!!.economiaComercial = 1
                    }

                    codigoCategoria = if (categoria == true) "C1" else "C"

                    this.esgotoComercial = apuraMinimoCategoria(
                            codigoCategoria, sigla[1]) * ligacao!!.economiaComercial
                }

                if (ligacao!!.economiaIndustrial > 0) {

                    if (ligacao!!.statusEconomia == "N") {
                        ligacao!!.economiaIndustrial = 1
                    }

                    codigoCategoria = if (categoria == true) "I1" else "I"

                    this.esgotoIndustrial = apuraMinimoCategoria(
                            codigoCategoria, sigla[1]) * ligacao!!.economiaIndustrial
                }

                if (ligacao!!.economiaPublica > 0) {

                    if (ligacao!!.statusEconomia == "N") {
                        ligacao!!.economiaPublica = 1
                    }

                    codigoCategoria = if (categoria == true) "P1" else "P"

                    this.esgotoPublica = apuraMinimoCategoria(codigoCategoria,
                            sigla[1]) * ligacao!!.economiaPublica
                }

                if (ligacao!!.economiaOutras > 0) {

                    if (ligacao!!.statusEconomia == "N") {
                        ligacao!!.economiaOutras = 1
                    }

                    codigoCategoria = if (categoria == true) "O1" else "O"

                    this.esgotoOutras = apuraMinimoCategoria(codigoCategoria,
                            sigla[1]) * ligacao!!.economiaOutras
                }
            }
        }

        // Tratamento de Esgoto
        if (ligacao!!.statusTratamentoEsgoto == "S") {

            if (ligacao!!.economiaResidencial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaResidencial = 1
                }

                codigoCategoria = if (categoria == true) "R1" else "R"

                this.tratamentoResidencial = apuraMinimoCategoria(
                        codigoCategoria, sigla[2]) * ligacao!!.economiaResidencial
            }

            if (ligacao!!.economiaComercial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaComercial = 1
                }

                codigoCategoria = if (categoria == true) "C1" else "C"

                this.tratamentoComercial = apuraMinimoCategoria(
                        codigoCategoria, sigla[2]) * ligacao!!.economiaComercial
            }

            if (ligacao!!.economiaIndustrial > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaIndustrial = 1
                }

                codigoCategoria = if (categoria == true) "I1" else "I"

                this.tratamentoIndustrial = apuraMinimoCategoria(
                        codigoCategoria, sigla[2]) * ligacao!!.economiaIndustrial
            }

            if (ligacao!!.economiaPublica > 0) {

                if (ligacao!!.statusEconomia != null && ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaPublica = 1
                }

                codigoCategoria = if (categoria) "P1" else "P"

                this.tratamentoPublica = apuraMinimoCategoria(codigoCategoria,
                        sigla[2]) * ligacao!!.economiaPublica
            }

            if (ligacao!!.economiaOutras > 0) {

                if (ligacao!!.statusEconomia == "N") {
                    ligacao!!.economiaOutras = 1
                }

                codigoCategoria = if (categoria == true) "O1" else "O"

                this.tratamentoOutras = apuraMinimoCategoria(codigoCategoria,
                        sigla[2]) * ligacao!!.economiaOutras
            }
        }

        // Desprezar o valor mínimo
        if (minimo == "S") {
            this.minimoAgua = 0.0
            this.minimoEsgoto = 0.0
            this.minimoEsgotoEspecial = 0.0
            this.minimoTratamento = 0.0

        } else {
            this.minimoAgua = (this.aguaResidencial + this.aguaComercial
                    + this.aguaIndustrial + this.aguaPublica + this.aguaOutras)

            this.minimoEsgoto = (this.esgotoResidencial + this.esgotoComercial
                    + this.esgotoIndustrial + this.esgotoPublica
                    + this.esgotoOutras)

            this.minimoTratamento = (this.tratamentoResidencial
                    + this.tratamentoComercial + this.tratamentoIndustrial
                    + this.tratamentoPublica + this.tratamentoOutras)

            // Água/Esgoto e Esgoto Especial
            if (ligacao!!.codigoFaturamento == 3
                    || ligacao!!.codigoFaturamento == 4
                    || ligacao!!.codigoFaturamento == 5) {
                this.minimoEsgotoEspecial = this.minimoEsgoto
            }

            // Esgoto Especial sem Esgoto
            if (ligacao!!.codigoFaturamento == 3 || ligacao!!.codigoFaturamento == 4) {
                this.minimoEsgoto = 0.0
            }

        }

        this.valorMinimo = (this.minimoAgua + this.minimoEsgoto
                + this.minimoTratamento + this.minimoEsgotoEspecial)
    }

    /**
     * Calculas as tarifas
     */
    @Throws(Exception::class)
    fun calculaTarifas() {

        var qtdEconomias: Int
        var consEconomia: Double
        var consEconEsgotoEspecial: Double
        var consFaturadoFaixaEE: Double
        var consFaturadoFaixa: Double

        var aguaResidencial = 0.0
        var aguaComercial = 0.0
        var aguaIndustrial = 0.0
        var aguaPublico = 0.0
        var aguaOutras = 0.0

        var esgResidencial = 0.0
        var esgComercial = 0.0
        var esgIndustrial = 0.0
        var esgPublico = 0.0
        var esgOutras = 0.0

        var esgEspResidencial = 0.0
        var esgEspComercial = 0.0
        var esgEspIndustrial = 0.0
        var esgEspPublico = 0.0
        var esgEspOutras = 0.0

        var tratResidencial = 0.0
        var tratComercial = 0.0
        var tratIndustrial = 0.0
        var tratPublico = 0.0
        var tratOutras = 0.0

        val categoria = this.defineCodigoCategoria()
        var codigoCategoria = ""

        qtdEconomias = (ligacao!!.economiaResidencial
                + ligacao!!.economiaComercial + ligacao!!.economiaIndustrial
                + ligacao!!.economiaPublica + ligacao!!.economiaOutras)


        qtdEconomias = if (qtdEconomias == 0)
            1
        else
            qtdEconomias


        // Só joga para o ConsumoFat para 10 se não for Cortada
        if (this.consumoFaturadoAgua == 0
                && ligacao!!.situacaoLigacao != "C"
                && ligacao!!.situacaoLigacao != "K") {
            this.consumoFaturadoAgua = 10
        }

        // PED:9240, EXISTE CREDITO NEGATIVO VINDO DA REFERENCIA ANTERIOR,
        // SUBTRAIR O RESIDUO PARA ENCONTRAR A FAIXA DE VALOR
        if (ligacao!!.credNegativoFatur < 0 && this.consumoFaturadoEsgoto > 0) {
            consFaturadoFaixaEE = (this.consumoFaturadoEsgoto + ligacao!!.credNegativoFatur).toDouble()
        } else {
            consFaturadoFaixaEE = this.consumoFaturadoEsgoto.toDouble()
        }

        if (this.consumoFaturadoEsgoto > 0) {
            if (ligacao!!.statusEconomia == "S") {
                consEconEsgotoEspecial = NumberUtils.truncN(this.consumoFaturadoEsgoto / qtdEconomias.toDouble(), 7)
                consFaturadoFaixaEE = NumberUtils.truncN(consFaturadoFaixaEE / qtdEconomias.toDouble(), 7)
            } else {
                consEconEsgotoEspecial = this.consumoFaturadoEsgoto.toDouble()
            }
        } else {
            consEconEsgotoEspecial = 0.0
        }

        consEconEsgotoEspecial = NumberUtils.round(consEconEsgotoEspecial, 3)
        consFaturadoFaixaEE = NumberUtils.round(consFaturadoFaixaEE, 3)

        if (ligacao!!.credNegativoFatur < 0 && this.consumoFaturadoAgua > 0) {
            consFaturadoFaixa = (this.consumoFaturadoAgua + ligacao!!.credNegativoFatur).toDouble()
        } else {
            consFaturadoFaixa = this.consumoFaturadoAgua.toDouble()
        }

        if (ligacao!!.statusEconomia == "S") {
            consEconomia = NumberUtils.truncN(this.consumoFaturadoAgua / qtdEconomias.toDouble(), 7)
            consFaturadoFaixa = NumberUtils.truncN(consFaturadoFaixa / qtdEconomias.toDouble(), 7)
        } else {
            consEconomia = this.consumoFaturadoAgua.toDouble()
        }

        consEconomia = NumberUtils.round(consEconomia, 3)
        consFaturadoFaixa = NumberUtils.round(consFaturadoFaixa, 3)

        if (ligacao!!.economiaResidencial > 0) {

            codigoCategoria = if (categoria == true) "R1" else "R"

            codigoCategoria = if (isExcecaoRES) "R" else codigoCategoria

            aguaResidencial = this.tarifas(codigoCategoria, consEconomia,
                    "A", ligacao!!.economiaResidencial, consFaturadoFaixa)

            esgResidencial = this.tarifas(codigoCategoria, consEconomia,
                    "E", ligacao!!.economiaResidencial, consFaturadoFaixa)

            esgEspResidencial = this.tarifas(codigoCategoria, consEconEsgotoEspecial,
                    "E", ligacao!!.economiaResidencial, consFaturadoFaixaEE)

            tratResidencial = this.tarifas(codigoCategoria, consEconomia,
                    "T", ligacao!!.economiaResidencial, consFaturadoFaixa)
        }

        if (ligacao!!.economiaComercial > 0) {

            codigoCategoria = if (categoria == true) "C1" else "C"

            aguaComercial = this.tarifas(codigoCategoria, consEconomia,
                    "A", ligacao!!.economiaComercial, consFaturadoFaixa)

            esgComercial = this.tarifas(codigoCategoria, consEconomia,
                    "E", ligacao!!.economiaComercial, consFaturadoFaixa)

            esgEspComercial = this.tarifas(codigoCategoria, consEconEsgotoEspecial,
                    "E", ligacao!!.economiaComercial, consFaturadoFaixaEE)

            tratComercial = this.tarifas(codigoCategoria, consEconomia,
                    "T", ligacao!!.economiaComercial, consFaturadoFaixa)

        }

        if (ligacao!!.economiaIndustrial > 0) {
            codigoCategoria = if (categoria == true) "I1" else "I"

            aguaIndustrial = this.tarifas(codigoCategoria, consEconomia,
                    "A", ligacao!!.economiaIndustrial, consFaturadoFaixa)

            esgIndustrial = this.tarifas(codigoCategoria, consEconomia,
                    "E", ligacao!!.economiaIndustrial, consFaturadoFaixa)

            esgEspIndustrial = this.tarifas(codigoCategoria, consEconEsgotoEspecial,
                    "E", ligacao!!.economiaIndustrial, consFaturadoFaixaEE)

            tratIndustrial = this.tarifas(codigoCategoria, consEconomia,
                    "T", ligacao!!.economiaIndustrial, consFaturadoFaixa)
        }

        if (ligacao!!.economiaPublica > 0) {
            codigoCategoria = if (categoria == true) "P1" else "P"

            aguaPublico = this.tarifas(codigoCategoria, consEconomia,
                    "A", ligacao!!.economiaPublica, consFaturadoFaixa)

            esgPublico = this.tarifas(codigoCategoria, consEconomia,
                    "E", ligacao!!.economiaPublica, consFaturadoFaixa)

            esgEspPublico = this.tarifas(codigoCategoria, consEconEsgotoEspecial,
                    "E", ligacao!!.economiaPublica, consFaturadoFaixaEE)

            tratPublico = this.tarifas(codigoCategoria, consEconomia,
                    "T", ligacao!!.economiaPublica, consFaturadoFaixa)
        }

        if (ligacao!!.economiaOutras > 0) {
            codigoCategoria = if (categoria == true) "O1" else "O"

            aguaOutras = this.tarifas(codigoCategoria, consEconomia,
                    "A", ligacao!!.economiaOutras, consFaturadoFaixa)

            esgOutras = this.tarifas(codigoCategoria, consEconomia,
                    "E", ligacao!!.economiaOutras, consFaturadoFaixa)

            esgEspOutras = this.tarifas(codigoCategoria, consEconEsgotoEspecial,
                    "E", ligacao!!.economiaOutras, consFaturadoFaixaEE)

            tratOutras = this.tarifas(codigoCategoria, consEconomia,
                    "T", ligacao!!.economiaOutras, consFaturadoFaixa)
        }

        // Tratamento de Esgoto
        if (ligacao!!.statusTratamentoEsgoto == "S") {
            this.valorTratamentoEsgoto = (tratResidencial
                    + tratComercial + tratIndustrial
                    + tratPublico + tratOutras)
        } else {
            this.valorTratamentoEsgoto = 0.0
        }

        when (ligacao!!.codigoFaturamento) {

            // Água
            1 -> {
                this.valorAgua = (aguaResidencial + aguaComercial + aguaIndustrial
                        + aguaPublico + aguaOutras)
                this.valorEsgoto = 0.0
                this.valorEsgotoEspecial = 0.0
            }

            // Agua e Esgoto
            2 -> {
                this.valorAgua = (aguaResidencial + aguaComercial + aguaIndustrial
                        + aguaPublico + aguaOutras)
                this.valorEsgoto = (esgResidencial + esgComercial
                        + esgIndustrial + esgPublico + esgOutras)
                this.valorEsgotoEspecial = 0.0
            }

            // Tratamento de Esgoto Especial
            3 -> {
                this.valorAgua = 0.0
                this.valorEsgoto = 0.0
                this.valorEsgotoEspecial = (esgEspResidencial
                        + esgEspComercial + esgEspIndustrial
                        + esgEspPublico + esgEspOutras)
            }

            // Água e Tratamento de Esgoto Especial
            4 -> {
                this.valorAgua = (aguaResidencial + aguaComercial + aguaIndustrial
                        + aguaPublico + aguaOutras)
                this.valorEsgoto = 0.0
                this.valorEsgotoEspecial = (esgEspResidencial
                        + esgEspComercial + esgEspIndustrial
                        + esgEspPublico + esgEspOutras)
            }

            // Água, Esgoto e Tratamento de Esgoto Especial
            else -> {
                this.valorAgua = (aguaResidencial + aguaComercial + aguaIndustrial
                        + aguaPublico + aguaOutras)
                this.valorEsgoto = (esgResidencial + esgComercial
                        + esgIndustrial + esgPublico + esgOutras)
                this.valorEsgotoEspecial = (esgEspResidencial
                        + esgEspComercial + esgEspIndustrial
                        + esgEspPublico + esgEspOutras)
            }
        }

    }

    /*
        * Calcula as tarifas
        */
    @Throws(Exception::class)
    fun tarifas(categoria: String, faturado: Double, cobranca: String,
                economia: Int, faturadoFaixa: Double): Double {
        var categoria = categoria

        var valorTarifa = 0.0
        var valorDeduzir = 0.0
        var consumoMinimo = 0.0
        var retorno = 0.0
        var faixa: Faixa? = null /* = Faixa("",
                "",
                0,
                0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                "",
                0.0,
                "",
                0.0,
                0.0,
                "")*/

        // sai fora do método
        if (faturado == 0.0) {
            return 0.0
        }
/*
        faixa.codigoCategoria = categoria
        faixa.tipoLigacao = ligacao!!.tipoLigacao
        faixa.codigoFaturamento = ligacao!!.codigoFaturamento*/

        this.faixas = dataBase!!.faixaDao()?.cascata(categoria, ligacao!!.codigoFaturamento, ligacao!!.tipoLigacao).blockingFirst();
        var entrou  = false;
        if (faixas !=null && !faixas.isEmpty()){
            for (faixaVar in faixas) {
                if ((NumberUtils.Companion.compare(faturado, faixaVar.consumoMinimo + 0.001 ) == 1 ) &&
                        NumberUtils.Companion.compare(faturado, faixaVar.consumoMaximo + 0.001 ) != 1){
                    if (faixaVar.codigoCategoria.equals(categoria) &&
                            faixaVar.codigoFaturamento.equals(ligacao!!.codigoFaturamento) &&
                            faixaVar.tipoLigacao.equals(ligacao!!.tipoLigacao)){
                        faixa = faixaVar
                        break
                    }
                }
            }
        }


        if (faixa != null) {

            consumoMinimo = faixa.consumoMinimo

            if (cobranca == "A") {
                valorTarifa = faixa.tarifaAgua
            } else if (cobranca == "E") {
                valorTarifa = faixa.tarifaEsgoto
            } else if (cobranca == "T") {
                valorTarifa = faixa.tarifaTratamento
                this.faixaTratamentoEsgoto = valorTarifa
            }

            // Água
            if (cobranca == "A") {

                if (faixa.sinalDeducao == "+") {
                    valorDeduzir = java.lang.Double.valueOf(faixa.deduzirAgua)
                } else {
                    valorDeduzir = java.lang.Double.valueOf(faixa.deduzirAgua) * -1
                }

                // Esgoto
            } else if (cobranca == "E") {

                if (faixa.sinalEsgoto == "+") {
                    valorDeduzir = faixa.deduzirEsgoto
                } else {
                    valorDeduzir = faixa.deduzirEsgoto * -1
                }

                // Tratamento
            } else if (faixa.sinalTratamento == "+") {

                if (faixa.sinalTratamento == "+") {
                    valorDeduzir = faixa.deduzirTratamento
                } else {
                    valorDeduzir = faixa.deduzirTratamento * -1
                }
            }

            retorno = NumberUtils.truncN(valorTarifa * faturado - valorDeduzir, 2)

            // Esgoto / Percentual de Esgoto sobre a Agua - Excecao
            if (cobranca == "E" && ligacao!!.percentualEsgoto > 0) {

                consumoMinimo = faixa.consumoMinimo
                valorTarifa = faixa.tarifaAgua

                if (faixa.sinalDeducao == "+") {
                    valorDeduzir = java.lang.Double.valueOf(faixa.deduzirAgua)
                } else {
                    valorDeduzir = java.lang.Double.valueOf(faixa.deduzirAgua) * -1
                }

                retorno = NumberUtils.truncN((valorTarifa * faturado - valorDeduzir) * ligacao!!.percentualEsgoto, 2)
            }


            // SE CAIR NA 1ª FAIXA, CONSIDERAR O MÍNIMO DA TARIFA
            // Ped:9240 - NÃO CONSIDERAR VALOR MÍNIMO DA PRIMEIRA FAIXA QUANDO HÁ RESIDO NEGATIVO
            if (consumoMinimo == 0.0 && faturado == faturadoFaixa) {

                categoria = categoria.substring(0, 1)

                // Água
                if (cobranca == "A") {

                    if (categoria == "R") {
                        retorno = this.aguaResidencial / economia
                    } else if (categoria == "C") {
                        retorno = this.aguaComercial / economia
                    } else if (categoria == "I") {
                        retorno = this.aguaIndustrial / economia
                    } else if (categoria == "P") {
                        retorno = this.aguaPublica / economia
                    } else if (categoria == "O") {
                        retorno = this.aguaOutras / economia
                    }

                    // Esgoto
                } else if (cobranca == "E") {

                    if (categoria == "R") {
                        retorno = this.esgotoResidencial / economia
                    } else if (categoria == "C") {
                        retorno = this.esgotoComercial / economia
                    } else if (categoria == "I") {
                        retorno = this.esgotoIndustrial / economia
                    } else if (categoria == "P") {
                        retorno = this.esgotoPublica / economia
                    } else if (categoria == "O") {
                        retorno = this.esgotoOutras / economia
                    }

                    // Tratamento
                } else if (cobranca == "T") {

                    if (categoria == "R") {
                        retorno = this.tratamentoResidencial / economia
                    } else if (categoria == "C") {
                        retorno = this.tratamentoComercial / economia
                    } else if (categoria == "I") {
                        retorno = this.tratamentoIndustrial / economia
                    } else if (categoria == "P") {
                        retorno = this.tratamentoPublica / economia
                    } else if (categoria == "O") {
                        retorno = this.tratamentoOutras / economia
                    }
                }
            }
        } else {
            this.statusErro = 56
            return 0.0
        }


        // Tratamento de Esgoto
        if (cobranca == "T") {
            if (ligacao!!.statusTratamentoEsgoto == "S" &&
                    this.statusTratEsgotoProporcional == "S" &&
                    ligacao!!.tipoLigacao == 1 &&
                    NumberUtils.compare(retorno, 0.0) == 1) {  // maior que zero
                retorno = retorno / this.quantidadeDiasConsumo * this.quantidadeDiasTratEsgoto
            }
        }

        retorno = NumberUtils.truncN(retorno * economia, 2)



        return retorno

    }

    /**
     * Gera Serviços
     */
    @Throws(Exception::class)
    fun geraServicos() {

        var valorServicos = 0.0

        valorServicos = this.buscaServicos()

        this.valorTotal = NumberUtils.round(
                this.valorTotal + valorServicos - this.valorNegado, 2)

    }

    /* *
        * Para as contas com valor total menor
        * que o limite mínimo de R$<n>. Isentar a conta e gerar o valor
        * para a próxima fatura.
        * @throws Exception
        */

    @Throws(Exception::class)
    fun verificarValorMinimo() {

        //buscar o valor mínimo da conta por categoria.
        var valMinimoFat = 0.0

        var categorias  = dataBase!!.categoriaDao().getCategoria(ligacao!!.codigoCategoria?.trim()!!).blockingFirst()
        if (categorias !=null && !categorias.isEmpty()){
            valMinimoFat  = categorias.get(0).valorMinimo;
        }

        if (this.valorTotal < valMinimoFat && this.valorTotal > 0) {
            val servicos = dataBase?.servicoDao()?.isParcelaIncorporada(this.numeroLigacao)?.blockingSingle()


            if (servicos != null && !servicos.isEmpty()) {
                // Está conta NÃO tem parcela incorporada.


                //Indica o valor do serviço que será gerado para o próximo faturamento
                //porque não atingiu o valor minimo este mês.
                valorMinNaoLancado = this.valorTotal

                //Zerar o valor dos serviços lançados em conta.
                for (servi in servicos) {
                    servi.valorParcela = 0.0
                    servi.valorImposto = 0.0
                    servi.referenciaParcela = "Val.Min"
                    servi.valorBaseImposto = 0.0

                    dataBase?.servicoDao()?.updateServico(servi)
                }

                // Isentar a conta dos valores de tarifa.
                this.valorTotal = 0.0

            }
        }


    }

    /**
     * Recebe os dados para gravação da rubrica
     */

    @Throws(Exception::class)
    fun gravarRubrica(codigoRubrica: Int, impostoBase: Double,
                      parcela: Double, imposto: Double, status: String) {

        var detalhes = ""

        val servico = Servico(0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0.0,
                0.0,
                0.0,
                "",
                "",
                0.0,
                "",
                0)

        var qtdServico = this.tarefa!!.quantidadeServicos
        qtdServico++
        this.tarefa!!.quantidadeServicos = qtdServico // TODO checar se qts vai sendo incrementada

        servico.numReg = Integer.valueOf(this.tarefa!!.quantidadeServicos)!!.toLong()
        servico.matricula = ligacao!!.numeroLigacao.toInt()
        servico.numeroAviso = ligacao!!.numeroAviso
        servico.anoEmissao = ligacao!!.anoLancamento
        servico.numeroEmissao = ligacao!!.numeroEmissao
        servico.numeroServico = 0
        servico.itemServico = 0
        servico.codigoRubrica = codigoRubrica
        servico.valorParcela = NumberUtils.round(parcela, 2)
        servico.valorImposto = imposto
        servico.valorBaseImposto = impostoBase
        servico.referenciaParcela = "01/01"
        servico.codigoTributo = ligacao!!.codigoTributo
        servico.statusDesconto = "N"
        servico.statusLancamento = status

        servico.quantidadeServicos = this.tarefa!!.quantidadeServicos // TODO VER TAREFA

        dataBase?.servicoDao()?.insertServico(servico)

        this.quantidadeLancamentos++

        var rubrica: Rubrica? = null
        var rubricas  = dataBase!!.rubricaDao().getRubrica(servico.codigoRubrica).blockingFirst()
        if (rubricas !=null && !rubricas.isEmpty()){
            rubrica = rubricas.get(0);
        }

        if (rubrica != null) {

            detalhes = formataLancamentos(rubrica.descricaoRubrica, parcela)

            this.lancamentos.put(this.quantidadeLancamentos - 1, detalhes)
        }


    }

    /**
     * Formata os lançamentos para impressão. Tamanho padrão são 35 caracteres.
     * Ajusta automaticamente a descrição da rúbrica e o valor.
     *
     */


    fun formataLancamentos(descricao: String?, valor: Double): String {

        var retorno = ""
        val tamanho = 34

        val df = DecimalFormat("###,##0.00")

        val valorDecimal = NumberUtils.zeroPad(df.format(valor),  10)

        //  NumberUtils.formatNumber(valor, 7, 2, "S")

        // a descrição pode ter no máximo 18 caracteres
        // o retorno TEM que ter sempre 35 caracteres
        // deve-se deixar um espaço em branco após o valor
        retorno = (descricao
                + Utils.space(tamanho - (descricao!!.length + valorDecimal.length))
                + valorDecimal + Utils.space(1))

        return retorno

    }

    @Throws(Exception::class)
    fun parametrosImpressao() {

        val dTotalDemaisLancamentos: Double
        var sTotalDemaisLancamentos = ""
        var detalhe = ""
        val detalheConta = arrayOfNulls<String>(20)
        var sanesalto = ""

        // Demais Lancamentos
        dTotalDemaisLancamentos = this.demaisLancamentos - this.demaisLancamentosNegados

        if (dTotalDemaisLancamentos == 0.0) {
            sTotalDemaisLancamentos = ""
        } else if (dTotalDemaisLancamentos < 0) {
            sTotalDemaisLancamentos = NumberUtils.zeroPad(NumberUtils.toString(dTotalDemaisLancamentos, 2),  7) + "-"


        } else if (dTotalDemaisLancamentos > 0) {
            sTotalDemaisLancamentos = NumberUtils.zeroPad(NumberUtils.toString(dTotalDemaisLancamentos, 2),  7) + " "

        }

        // Cascata
        this.cascata(this.consumoFaturadoAgua.toDouble())


        for (n in 0..19) {
            detalheConta[n] = Utils.space(70)
        }

        for (i in 0..this.imprimeFaixas.size -1) {

            detalhe = ""

            // Linhas com faixa de tarifa
            if (i < this.quantidadeFaixas) {
                try {

                    detalhe = this.imprimeFaixas.get(i).toString().substring(0,
                            33) + Utils.space(2)
                } catch (e: Exception){
                    Log.e("Errou", detalhe )
                }
            }

            // Linhas com lançamento
            if (i < this.quantidadeLancamentos) {

                // Linhas com lançamento e sem faixa
                if (i >= this.quantidadeFaixas && i <= 11) {

                    // Linha do tratamento de esgoto
                    if (i == 11) {

                        if (ligacao!!.statusTratamentoEsgoto == "S") {

                            sanesalto = NumberUtils.zeroPad(NumberUtils.toString(this.faixaTratamentoEsgoto, 4),  7)

                            detalhe = "T. Trat. SANESALTO"
                            detalhe += (Utils
                                    .space(33 - (detalhe.length + sanesalto
                                            .length))
                                    + sanesalto + Utils.space(2))

                        } else {
                            detalhe += Utils.space(35)
                        }
                    } else {
                        detalhe += Utils.space(35)
                    }
                }

                detalhe += this.lancamentos.get(i)

                // Linha sem lançamentos
            } else {

                // Linha do tratamento de esgoto
                if (i == 11) {

                    if (ligacao!!.statusTratamentoEsgoto == "S") {
                        sanesalto = NumberUtils.zeroPad(NumberUtils.toString(this.faixaTratamentoEsgoto, 4),  7)

                        detalhe = "T. Trat. SANESALTO"
                        detalhe += Utils.space(33 - (detalhe.length + sanesalto.length)) + sanesalto + Utils.space(2)//+ StringUtil.space(35); era 33
                    } else {
                        detalhe += Utils.space(35)
                    }
                } else {
                    detalhe += Utils.space(70)
                }

                // Linha dos demais lançamentos
                if (i == 11) {

                    if (sTotalDemaisLancamentos != "") {
                        detalhe = Utils.setField(detalhe + "Demais lancamentos " + Utils.setField(sTotalDemaisLancamentos, 16, 0), 35, 1)
                    } else {
                        detalhe = Utils.setField(detalhe, 70, 1)
                    }

                }

            }

            detalheConta[i] = detalhe

        }


        cascata!!.numeroChave = Integer.parseInt(ligacao!!.numReg)
        cascata!!.faixa[0] = detalheConta[0]
        cascata!!.faixa[1] = detalheConta[1]
        cascata!!.faixa[2] = detalheConta[2]
        cascata!!.faixa[3] = detalheConta[3]
        cascata!!.faixa[4] = detalheConta[4]
        cascata!!.faixa[5] = detalheConta[5]
        cascata!!.faixa[6] = detalheConta[6]
        cascata!!.faixa[7] = detalheConta[7]
        cascata!!.faixa[8] = detalheConta[8]
        cascata!!.faixa[9] = detalheConta[9]
        cascata!!.faixa[10] = detalheConta[10]
        cascata!!.faixa[11] = detalheConta[11]
        cascata!!.faixa[12] = detalheConta[12]
        cascata!!.faixa[13] = detalheConta[13]
        cascata!!.faixa[14] = detalheConta[14]
        cascata!!.faixa[15] = detalheConta[15]
        cascata!!.faixa[16] = detalheConta[16]
        cascata!!.faixa[17] = detalheConta[17]
        cascata!!.faixa[18] = detalheConta[18]
        cascata!!.faixa[19] = detalheConta[19]

        //  CascataDAO.getInstance().gravaParametrosImpressao(cascata); // TODO GRAVAR

    }

    @Throws(Exception::class)
    fun cascata(faturamento: Double) {

        var faixa = Faixa("",
                "",
                0,
                0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                "",
                0.0,
                "",
                0.0,
                0.0,
                "")
        var agua = ""
        var esgoto = ""
        var maximo = 0
        var minimo = 0
        var anterior: Int
        var imprimeFaixa = ""
        val tab = Utils.space(3)

        faixa.codigoCategoria = if (this.isExcecaoRES) "R" else ligacao!!.codigoCategoria!!.trim { it <= ' ' }
        faixa.codigoFaturamento = ligacao!!.codigoFaturamento
        faixa.tipoLigacao = ligacao!!.tipoLigacao

        this.faixas = dataBase!!.faixaDao().cascata(faixa.codigoCategoria!!, faixa.codigoFaturamento, faixa.tipoLigacao).defaultScheduler().blockingFirst()


        if (faixas.size == 0) {
            this.statusErro = 56
        }

        if (NumberUtils.compare(faturamento, 0.0) == 0) {
            this.quantidadeFaixas++
            this.imprimeFaixas.set((quantidadeFaixas - 1), Utils.space(37))
            return
        }

        val tamanho = faixas.size

        for (i in 0 until tamanho) {

            faixa = faixas[i]

            this.quantidadeFaixas++
            anterior = this.quantidadeFaixas - 1
            maximo = faixa.consumoMaximo.toInt()
            minimo = faixa.consumoMinimo.toInt()

            if (NumberUtils.compare(faturamento, minimo + 0.001) == 1 && NumberUtils.compare(faturamento, maximo.toDouble()) <= 0) {

                if (minimo == 0) {

                    agua = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaAgua * maximo, 4),  9), 9, 0)
                    esgoto = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaEsgoto * maximo, 4),  9), 9, 0)


                    imprimeFaixa = (" ate"
                            + tab
                            + Utils.setField(NumberUtils.toString(maximo.toDouble(), 0), 2, 1)

                            + Utils.setField(NumberUtils.toString(maximo.toDouble(), 0), 6, 0))
                    imprimeFaixa += agua
                    imprimeFaixa += esgoto

                    this.imprimeFaixas.set(anterior, imprimeFaixa)

                } else {

                    if (maximo > 99999) {

                        imprimeFaixa = (" acima "
                                + Utils.setField(NumberUtils.toString(minimo.toDouble(), 0), 2, 1)
                                + Utils.setField(NumberUtils.toString((faturamento - minimo).toInt().toDouble(), 0), 6, 0))

                    } else {

                        imprimeFaixa = (Utils.setField(minimo.toString(), 3, 0)
                                + " a "
                                + Utils.setField(maximo.toString(), 3, 0)
                                + Utils.setField((NumberUtils.toString((faturamento - minimo), 0).toString()), 6, 0))
                    }

                    agua = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaAgua, 4),  9), 9, 0)
                    esgoto = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaEsgoto, 4),  9), 9, 0)


                    imprimeFaixa += agua
                    imprimeFaixa += esgoto

                    this.imprimeFaixas.set(anterior, imprimeFaixa)


                }

            //    return

            } else {

                if (minimo == 0) {

                    agua =   Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaAgua, 4),  9), 9, 0)

                    esgoto = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaEsgoto * maximo, 4),  9), 9, 0)

                    imprimeFaixa = (" ate   "
                            + Utils.setField(maximo.toString(), 3, 0)
                            + Utils.setField(maximo.toString(), 6, 0))
                    imprimeFaixa += agua
                    imprimeFaixa += esgoto

                    this.imprimeFaixas.set(anterior, imprimeFaixa)

                } else {

                    agua = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaAgua, 4),  9), 9, 0)
                    esgoto = Utils.setField(NumberUtils.zeroPad(NumberUtils.toString(faixa.tarifaEsgoto, 4),  9), 9, 0)

                    imprimeFaixa = (Utils.setField(minimo.toString(), 3, 0)
                            + " a "
                            + Utils.setField(maximo.toString(), 3, 0)
                            + Utils.setField((maximo - minimo).toString(), 6, 0))
                    imprimeFaixa += agua
                    imprimeFaixa += esgoto

                    this.imprimeFaixas.set(anterior, imprimeFaixa)

                }

            }

        }

    }

    fun buscaConsumos() {

        var pulaMes = 1
        var pulaAno = 3
        var pulaConsumo = 7

        val _78 = 78
        val treze = 13

        val consumos = arrayOfNulls<String>(5)

        // Linha 15 a 20 (Faixas)
        for (i in 14..19) {

            consumos[i] = (ligacao!!.historicoConsumo!!.substring(pulaMes,
                    pulaMes + 2)
                    + "/"
                    + ligacao!!.historicoConsumo!!
                    .substring(pulaAno, pulaAno + 4))

            consumos[i] += ligacao!!.historicoConsumo!!.substring(pulaConsumo,
                    pulaConsumo + 7)
            consumos[i] += Utils.space(4)

            consumos[i] = (consumos[i]
                    + ligacao!!.historicoConsumo!!.substring(pulaMes + _78,
                    pulaMes + _78 + 2)
                    + "/"
                    + ligacao!!.historicoConsumo!!.substring(pulaAno + _78,
                    pulaAno + _78 + 4))

            consumos[i] += ligacao!!.historicoConsumo!!.substring(
                    pulaConsumo + _78, pulaConsumo + _78 + 7)

            pulaMes += treze
            pulaAno += treze
            pulaConsumo += treze

        }

    }

    /**
     * Busca os servicos por ligação, e atribui valores às variáveis
     * relacionadas a valores
     *
     */

    @Throws(Exception::class)
    fun buscaServicos(): Double {

        var resultado = 0.0
        var servico = Servico(0, 0, 0L, 0,
                0, 0, 0, 0, 0, 0.0,
                0.0, 0.0, "", "", 0.0, "",
                0)
        var rubrica: Rubrica? = Rubrica(0, 0, "", "")
        var valorServico = 0.0
        var valor = NumberUtils.truncN(this.valorTotal, 2)
        var saldo = 0.0
        var desconto = 0
        var contador = 0

        var tamanho_descricao = 0
        var descricao = ""

        this.demaisLancamentos = 0.0
        this.demaisLancamentosNegados = 0.0

        var lista :List<Servico>  =  dataBase!!.servicoDao().getServicos(ligacao!!.numeroLigacao).blockingFirst()


        val tamanho = lista.size

        for (i in 0 until tamanho) {

            servico = lista[i]

            if (servico.statusLancamento == "N") {

                saldo = 0.0
                valorServico = 0.0


                desconto = if (servico.statusDesconto == "S") -1 else 1

                var rubrica: Rubrica? = null
                var rubricas  = dataBase!!.rubricaDao().getRubrica(servico.codigoRubrica).blockingFirst()
                if (rubricas !=null && !rubricas.isEmpty()){
                    rubrica = rubricas.get(0);
                }
                if (rubrica != null) {

                    if (desconto == 1 || desconto == -1 && NumberUtils.compare(
                                    servico.valorParcela, valor) != 1) {
                        valorServico = servico.valorParcela
                        saldo = 0.0
                    } else {
                        if (desconto == -1 && valor > 0) {
                            saldo = servico.valorParcela - valor
                            valorServico = valor
                        }
                    }

                    if (desconto == 1 || valor > 0) {

                        if (this.quantidadeLancamentos <= 10) {

                            this.quantidadeLancamentos += 1
                            descricao = descricaoRubrica(rubrica)
                            this.lancamentos.put(this.quantidadeLancamentos - 1, descricao )

                            if (rubrica.statusReferencia == "S") {
                                descricao = this.lancamentos.get(this.quantidadeLancamentos - 1)
                                        .toString() + " "

                                if (servico.referenciaParcela!!.trim { it <= ' ' } != "") {
                                    descricao += servico.referenciaParcela

                                }

                                this.lancamentos.set(this.quantidadeLancamentos - 1, descricao)
                            }
                        }

                        descricao = this.lancamentos.get(this.quantidadeLancamentos - 1)
                                .toString()
                        tamanho_descricao = descricao.length

                        if (desconto == 1) {

                            if (this.quantidadeLancamentos <= 11 && contador == 0) {

                                descricao += NumberUtils.zeroPad(NumberUtils.toString(servico.valorParcela, 2),
                                        7)

                                /*  NumberUtils
                                  .formatNumber(servico.valorParcela, 7,
                                          2, "S")*/
                                // (35 - tamanho_descricao), 0);
                                this.lancamentos.set(this.quantidadeLancamentos - 1, descricao)
                            } else {

                                this.demaisLancamentos = this.demaisLancamentos + servico.valorParcela * desconto

                            }

                            resultado = (resultado + servico.valorParcela) * desconto

                            if (servico.numeroServico > 0) {
                                valor = valor + servico.valorParcela
                            }

                        } else {

                            this.valorNegado += valorServico

                            if (this.quantidadeLancamentos <= 11 && contador == 0) {

                                descricao += NumberUtils.zeroPad(NumberUtils.toString(valorServico, 2),  7) + "-"
                                this.lancamentos.set(this.quantidadeLancamentos - 1, descricao)
                            } else {
                                this.demaisLancamentosNegados += valorServico
                            }

                            valor -= valorServico
                        }
                        servico.statusLancamento = "S"
                        servico.saldoServico = saldo
                        dataBase?.servicoDao()?.updateServico(servico)

                    }

                } else {
                    this.statusErro = 58
                }
            }

            if (this.quantidadeLancamentos == 11) {
                contador += 1
            }

        }

        return resultado

    }

    private fun descricaoRubrica(ru: Rubrica?): String {

        var rubrica = ""
        if (ru != null) {
            val max = if (ru.statusReferencia == "S") 10 else 18
            val tamanho = if (ru.descricaoRubrica!!.length > max) max - 1 else ru.descricaoRubrica!!.length
            if (tamanho != 0) {
                rubrica = ru!!.descricaoRubrica!!.substring(0, tamanho) + Utils.space(max - ru!!.descricaoRubrica!!.substring(0,
                        tamanho).length)
            } else {
                rubrica = Utils.space(max)
            }
        } else {
            rubrica = Utils.space(10)
        }
        return rubrica
    }

    /*  *
        * Define o valor do status_registro para a ligação e a visita
        *
        * @author balarini
        * @since 05/12/2008
   */

    fun defineStatusRegistro() {

        // Se o Registro ainda não havia sido lido anteriormente
        if (this.statusRegistro == 0) {

            // Se não Houve impedimento
            if (this.impedimento == 1) {

                // Incrementa a Variável de Estatística
                this.lidos += 1
                // grava
                ligacao!!.statusRegistro = STATUS_LIDO.toString()
                this.statusRegistro = STATUS_LIDO
            } else {
                // Incrementa a Variável de Estatística
                this.impedimentoEstatistica += 1
                // grava
                ligacao!!.statusRegistro = STATUS_IMPEDIMENTO.toString()
                this.statusRegistro = STATUS_IMPEDIMENTO
            }
        }

        // Se o Registro já havia sido lido anteriormente e era uma Leitura
        // Normal
        if (this.statusRegistro == 1) {

            // Se é Impedimento
            if (this.impedimento == 0) {
                this.impedimentoEstatistica += 1
                this.lidos -= 1

                // grava
                ligacao!!.statusRegistro = STATUS_IMPEDIMENTO.toString()
                this.statusRegistro = STATUS_IMPEDIMENTO
            }
        }

        // Se o Registro já havia sido lido anteriormente e era um Impedimento
        if (this.statusRegistro == 2) {

            // Se Leitura Normal
            if (this.impedimento == 1) {

                // Incrementa e decrementa as Variáveis de Estatística
                this.impedimentoEstatistica -= 1
                this.lidos += 1

                // grava
                ligacao!!.statusRegistro = STATUS_IMPEDIMENTO.toString()
                this.statusRegistro = STATUS_IMPEDIMENTO
            }
        }
    }

    /**
     * Calcula a fatura, de acordo com o consumo e o tipo.
     */


    @Throws(Exception::class)
    fun calculaFatura() {

        this.aguaResidencial = 0.0
        this.aguaComercial = 0.0
        this.aguaIndustrial = 0.0
        this.aguaPublica = 0.0
        this.aguaOutras = 0.0
        this.esgotoResidencial = 0.0
        this.esgotoComercial = 0.0
        this.esgotoIndustrial = 0.0
        this.esgotoPublica = 0.0
        this.esgotoOutras = 0.0
        this.tratamentoResidencial = 0.0
        this.tratamentoComercial = 0.0
        this.tratamentoIndustrial = 0.0
        this.tratamentoPublica = 0.0
        this.tratamentoOutras = 0.0

        this.geraItens()

        this.geraServicos()

        this.verificarValorMinimo()

    }

    /**
     * Configura a visita como impeditiva se foi informado um código de leitura e nenhuma leitura.
     */


    fun setOcorrenciaImpeditiva() {

        if (this.codigoLeitura > 0 && this.leituraAtual == 0) {
            this.ocorrenciaImpeditiva = true
        }

    }

    /* *
        * Configura a visita como impeditiva se foi informado um código de leitura
        * e nenhuma leitura, usado para verificar se a leitura foi informada como zero.
        **/

    fun setOcorrenciaImpeditiva(uiLeitura: String) {

        if (this.codigoLeitura > 0 && uiLeitura == "") {
            this.ocorrenciaImpeditiva = true
        }

    }

    /*   *
        * Realiza a validação da leitura e/ou código de leitura informado(s) pelo
        * colaborador
       */
    /*
       public void validarLeitura(int tentativas) throws    Exception {

           if (((this.leituraAtual != this.ultimaDigitada) || tentativas == 1)
                   && !isOcorrenciaImpeditiva() && this.leituraAtual > 0) {

               this.ultimaDigitada = this.leituraAtual;

               Sound.beep();

               // consumo igual ao último
               if (this.leituraAtual == ligacao.leituraAnterior) {
                   throw new MessageException(MessageException.CODE[0]);

                   // confirma a leitura
               } else if (this.leituraAtual < ligacao.leituraAnterior) {
                   throw new MessageException(MessageException.CODE[1]);

                   // maior que o número de ponteiros
               } else if (this.leituraAtual > ligacao.leituraMaxima) {
                   throw new MessageException(MessageException.CODE[2]);

                   // Fora de faixa
               } else if (this.leituraAtual < (ligacao.leituraAnterior + ligacao.consumoMinimoB)) {
                   throw new MessageException(MessageException.CODE[3]);
               } else if (this.leituraAtual > (ligacao.leituraAnterior + ligacao.consumoMaximoB)) {
                   throw new MessageException(MessageException.CODE[3]);
               }
           }

       }*/


    // * Atualiza o status da ligação

    fun atualizarStatus() {

        if (isOcorrenciaImpeditiva) {

            this.statusRegistro = STATUS_IMPEDIMENTO
            ligacao!!.statusRegistro = STATUS_IMPEDIMENTO.toString()
        } else {
            this.statusRegistro = STATUS_LIDO
            ligacao!!.statusRegistro = STATUS_LIDO.toString()
        }
    }


    //  * Verifica se a conta deverá ser retida ou bloqueada pelo sistema.
    @Throws(Exception::class)
    fun verificaRetencao(): Int {

        var valorLimite: Double
        val maximoMedido: Int

        // critério de retenção
        var entrega = 0

        valorLimite = 0.0

        var categoria: Categoria? = null
        var categorias  = dataBase!!.categoriaDao().getCategoria(ligacao!!.codigoCategoria!!.trim()).blockingFirst()
        if (categorias !=null && !categorias.isEmpty()){
            valorLimite  = categorias.get(0).valorLimite;
        }
        maximoMedido = ligacao!!.consumoMedio * 5

        // Erro de parâmetros
        if (this.statusErro != 0) {
            entrega = this.statusErro

        } else if (this.leituraAtual == 0 && this.consumoMedido > 0) {
            entrega = 63

            // Ligação cortada com valor de conta menor que o mínimo.
        } else if ((ligacao!!.situacaoLigacao == "C" || ligacao!!.situacaoLigacao == "K") && valorMinNaoLancado > 0) {
            entrega = 64

            // Ligações Cortadas
        } else if (ligacao!!.situacaoLigacao == "C") {
            entrega = 59

            // Ligações corte a pedido
        } else if (ligacao!!.situacaoLigacao == "K") {
            entrega = 59

        } else if (this.consumoMedido >= maximoMedido && NumberUtils.compare(ligacao!!.consumoMedio.toDouble(),
                        ligacao!!.consumoMinimo.toDouble()) != -1) {
            entrega = 60

        } else if (this.valorTotal > valorLimite) {
            entrega = 52

            // Código de Leitura c/ Sta_Emissao = "N"
        } else if (if (ligacao!!.tipoLigacao > 0)
                    this.statusEmissao == "N"
                else
                    this.ocorrencia!!.statusEmissao == "N") {

            entrega = 53

            // Endereço de Entrega Especial
        } else if (ligacao!!.codigoRemessa != 1) {
            entrega = 51
        }

        if (entrega != 0) {
            /*     this.tipoEntrega = TarefaDAO.getInstance().buscaDominio(
                               "DA_TPO_ENTREGA", entrega); // TODO BUSCAR*/
            this.mensagemEntrega = ("CONTA RETIDA (" + entrega
                    + ") ")
        }

        return entrega
    }

    /*  *
        * Método para conferir se o código de categoria tem o caractere 1 como
        * segundo dígito
        *
       */


    fun defineCodigoCategoria(): Boolean {

        val categoria: Boolean
        categoria = if (ligacao!!.codigoCategoria != null) ligacao!!.codigoCategoria!!.substring(1, 2) == "1" else false

        return categoria

    }

    @Throws(Exception::class)
    fun apuraDiasTratEsgotoProporcional(datTratEsgotoProporcional: String?) {

        val sdf = SimpleDateFormat("yyyyMMdd")


        var cal = Calendar.getInstance()
        cal.time = sdf.parse(datTratEsgotoProporcional)

        val firstDate = cal.time

        cal = Calendar.getInstance()
        val secondDate = cal.time

        val diff = secondDate.time - firstDate.time

        val calculoData = diff.toInt()
        if (calculoData > this.quantidadeDiasConsumo) {
            this.quantidadeDiasTratEsgoto = this.quantidadeDiasConsumo
        } else if (calculoData <= 0) {
            this.quantidadeDiasTratEsgoto = 0
        } else {
            this.quantidadeDiasTratEsgoto = calculoData
        }

    }


    @Throws(Exception::class)
    fun calculaConsumo() {
        resgatarParametros()

        if (this.ocorrenciaImpeditiva) {
            this.quantidadeTentativas = 0
        }

        this.quantidadeFaixas = 0
        this.criterioFaturamento = ligacao?.criterioFaturamento!!

        this.apuraMinimo()

        // Ligações Hidrometradas
        if (ligacao?.tipoLigacao!! > 0) {

            this.apuraMedido()

            this.verificaTroca()

            this.apura900()

            this.apuraExcecao()

            this.apuraPipa()

            this.apuraFaturado()

            // Realizar leituras nas ligações de corte a pedido
            if ((ligacao?.situacaoLigacao == "C" || ligacao?.situacaoLigacao == "K") && this.consumoMedido === 0) {
                // Zera o faturado para ligações cortadas com leitura igual a
                // anterior
                this.consumoFaturadoAgua = 0
            }

            // Estimada
        } else {

            this.apuraPipa()

            // Ativa
            if (ligacao?.situacaoLigacao == "A") {
                this.apuraExcecao()

                if (ligacao?.criterioExcecao != 10
                        && ligacao?.criterioExcecao != 21
                        && ligacao?.criterioExcecao != 51) {
                    this.apuraFaturado()
                } else {
                    this.apuraEstimado()
                }

            } else if (ligacao?.situacaoLigacao == "C" || ligacao?.situacaoLigacao == "K") {
                this.consumoFaturadoAgua = 0
            }
        }
    }


    companion object {

        val STATUS_NAOLIDO = 0

        val STATUS_LIDO = 1

        val STATUS_IMPEDIMENTO = 2

        private val COD_LEIT_INTERNO_900 = 900

        private val COD_LEIT_INTERNO_901 = 901

        private val COD_LEIT_INTERNO_902 = 902

        private val COD_LEIT_INTERNO_903 = 903

        private val COD_LEIT_INTERNO_904 = 904

        private val COD_LEIT_INTERNO_905 = 905

        private val COD_LEIT_INTERNO_906 = 906

        private val COD_LEIT_INTERNO_907 = 907

        private val COD_LEIT_INTERNO_908 = 908
    }

}
