package br.com.iresult.gmfmobile.model

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import java.util.*
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.bean.Parametro
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.utils.NumberUtils
import br.com.iresult.gmfmobile.utils.disposedBy
import org.apache.commons.lang3.math.NumberUtils.toInt


/**
 * Created by victorfernandes on 17/02/19.
 */
@SuppressLint("CheckResult")
class Coleta {

    val STATUS_NAOLIDO = 0

    val STATUS_LIDO = 1

    val STATUS_IMPEDIMENTO = 2

    var dataBase: AppDataBase? = null

    val TIPO_LEITURA = "L"

    val TIPO_TESTE = "T"

    val TIPO_SAIDA_IMPRESSORA_ZEBRA = "Z"

    val TIPO_SAIDA_TELA = "T"

    var tipo_saida = TIPO_SAIDA_IMPRESSORA_ZEBRA

    var numeroReg: Int = 0

    // COD_RUBR - Liga00
    var rubricaAgua: Int = 0

    var rubricaEsgoto: Int = 0

    var rubricaEsgotoEspecial: Int = 0

    var rubricaDesconto: Int = 0

    var rubricaEnte: Int = 0

    var rubricaTratamento: Int = 0

    // FAT_RED - Liga00
    var fatorReducaoAgua: Double = 0.toDouble()

    var fatorReducaoEsgoto: Double = 0.toDouble()

    var fatorReducaoEsgotoEspecial: Double = 0.toDouble()

    var fatorReducaoTratamento: Double = 0.toDouble()

    // DSC_ALIQ - Liga00
    var aliquotaAgua: Double = 0.toDouble()

    var aliquotaEsgoto: Double = 0.toDouble()

    var aliquotaEsgotoEspecial: Double = 0.toDouble()

    var aliquotaDesconto: Double = 0.toDouble()

    var aliquotaEnte: Double = 0.toDouble()

    var aliquotaTratamento: Double = 0.toDouble()

    var dataInicial: String = ""

    var dataFinal: String = ""

    var nomeTarefa: String = ""

    var numeroSerial: String = ""

    var anoMedicao: Int = 0

    var mesMedicao: Int = 0

    var medicao: String = ""

    var proximaLeitura: Date? = null

    var mensagem: String = ""

    var lido: Int = 0

    var impedimento: Int = 0

    var total: Int = 0

    var naoEntregue: Int = 0

    var impresso: Int = 0

    var quantidadeServicos: Int = 0

    var codigoColaborador: Int = 0

    var wherePesqProx = ""

    var roteiroInvertido = false

    //public String tipo = TIPO_TESTE;  //Tarefa de Teste

    var tipo = TIPO_LEITURA  //Tarefa de Teste

    var statusTratEsgotoProporcional: String? = null

    var dataTratEsgotoProporcional: String? = null;

    var staFatProporDia: String = ""

    var qtdDiasFaturar: Int = 0

    var staEmiteBoletoNotif: String = ""

    var valLimiteBoletoNotif: Double = 0.toDouble()

    var valorLimiteSocial: Int = 0




    /**
     * Contabiliza a quantidade de registros lidos, impedidos, não entregues e
     * impressos
     */
    @Throws(Exception::class)
    fun gerarEstatistica() {

        var total = 0
        var impedim = 0
        var lido = 0
        var naoEntregue = 0
        var impresso = 0
        var listaStatus: List<String>
        // Verificação dos registros da tabela LIGA01

        listaStatus = dataBase?.ligacaoDao()?.searchAllStaReg()?.blockingSingle()!!;

        var tamanho = listaStatus.size

        for (i in 0 until tamanho) {

            if (listaStatus.get(i).toInt() > 0) {

                // Quantidade de registros com impedimento
                if (listaStatus.get(i).toInt() === STATUS_IMPEDIMENTO) {
                    impedim++
                } else { // Quantidade de registros lidos
                    lido++
                }
            }
            total++
        }

        // Verificação dos registros da tabela LIGA02
       // listaStatus =  TarefaDAO.getInstance().listarStatusLigacao("STA_REG", Constantes.TABLIGACAO2) // TODO buscar ma lig02
     //   val listaEntrega = TarefaDAO.getInstance().listarStatusLigacao("TPO_ENTR", Constantes.TABLIGACAO2)

    /*    tamanho = listaStatus.size

        for (i in 0 until tamanho) {

            if (Convert.toInt(listaStatus[i]) > 0) {

                impresso++ // Quantidade de registros impressos utilizada para
                // a emissão do dados coleta

                // Contabiliza os tipos de Entrega = 00 (Não Entregue)
                if (listaEntrega[i].substring(0, 2) == "00") {
                    naoEntregue++
                }
            }
        }*/

        this.lido = lido
        this.impedimento = impedim
        this.total = total
        this.naoEntregue = naoEntregue
        this.impresso = impresso
    }

    /**
     * Verifica se todas as visitas foram realizadas
     */
   /*
    @Throws(Exception::class)
    fun isRoteiroCompleto(): Boolean {

        var ret = false

        this.gerarEstatistica()

        if (this.lido + this.impedimento == total) {
            ret = true
        }

        return ret
    }*/

    /**
     * Cria o arquivo de controle que indica que visitas foram realizadas A
     * existência desse arquivo indica que existem visitas a serem retornadas do
     * coletor.
     */
    /*
    fun criarArquivoIndAlter() {
        // FileUtil.createNewFile(Constantes.DATA_PATH, Constantes.SISTEMA + "_OK.CHK")
    }
*/
    /**
     * Atualiza o sta_reg do liga01 para 0 (não lido), para resolver o problema
     * de quando ocorrer travamento e apenas o liga01 tiver sido atualizado, ou
     * seja o liga01 ficou como lido e o liga02 como não lido.
     */
    /*
    @Throws(Exception::class)
    fun verificarStatus() {

        // Busca as ligações com leitura não realizada na tabela liga02
        val listaStatus = LigacaoDAO.getInstance().gerNumRegErro()

        var ligacao: Ligacao
        val tamanho = listaStatus.size
        var qtdServExcluir = 0

        for (i in 0 until tamanho) {

            // Busca o status da ligação da tabela liga01
            ligacao = LigacaoDAO.getInstance().buscarLigacao(Convert.toInt(listaStatus[i]))

            // Se a visita estiver marcada como realizada na tabela liga01,
            // atualiza o status para não realizada(0)
            if (ligacao.statusRegistro > 0) {
                ligacao.action = BaseBean.UPDATE
                ligacao.statusRegistro = Ligacao.STATUS_NAOLIDO // --> Não realzada
                LigacaoDAO.getInstance().gravar(ligacao)

                // Verificar se existe serviços adicionados pelo sistema Tinos
                // Ped: 8434 - Excluir os serviços
                qtdServExcluir = ligacao.verificaServicos(ligacao.numeroLigacao)

                // Atualiza a quantidade de servicos na tabela header
                if (qtdServExcluir > 0) {
                    this.quantidadeServicos = this.quantidadeServicos - qtdServExcluir
                    ServicoDAO.gravaServicoHeader(this.quantidadeServicos)
                }

            }
        }
    }*/

    /**
     * Verifica se a data informada está compreendida entre a data inicial e
     * final da tabela de parâmetros
     */
    /*
    @Throws(Exception::class)
    fun validarDataLeitura(dataAtual: String): Boolean {

        val inicio = Date(this.dataInicial)
        val fim = Date(this.dataFinal)
        val atual = Date(dataAtual)
        var retorno = true

        if (this.dataInicial == "") {
            this.janelaErro("CONFIRA A DATA INICIAL")
            MainWindow.getMainWindow().exit(0)
        }

        if (this.dataFinal == "") {
            this.janelaErro("CONFIRA A DATA FINAL")
            MainWindow.getMainWindow().exit(0)
        }

        if (atual.year < 2004) {
            this.janelaErro("VERIFIQUE O ANO")
        }

        val iDataInicial = inicio.getDateInt()
        val iDataFinal = fim.getDateInt()
        val iDataAtual = atual.getDateInt()

        if (iDataAtual > iDataFinal || iDataAtual < iDataInicial) {
            janelaErro("A data deve estar entre:|" + this.dataInicial + " e "
                    + this.dataFinal)
            retorno = false
        }

        return retorno

    }*/

    /**
     * Método que reinicializa a tarefa para não realizada
     */
        /*
        @Throws(Exception::class)
        fun reinicializarTarefa() {

            // Atualiza o status para 0 (NÃO LIDO) de todas as ligações
            // da tabela LIGA01
            LigacaoDAO.zerarLiga01()

            // Atualiza o status para 0 (NÃO LIDO) de todas as ligações
            // da tabela LIGA02
            LigacaoDAO.zerarLiga02()

            // Exclui os serviços incluídos e atualiza o status para N
            // dos serviços lançados
            ServicoDAO.zerarServicos()

            // Retorna a quantidade de serviços da tabela SERV01
            val qtdServicos = TarefaDAO.getInstance().getQtdServicos()

            // Atualiza a quantidade de serviços na tabela Header(SERV01)
            ServicoDAO.gravaServicoHeader(qtdServicos)

            // Exclui os parâmetros de impressão incluídos na tabela LIGA00
            CascataDAO.excluirParametrosImpressao()

        }*/



}