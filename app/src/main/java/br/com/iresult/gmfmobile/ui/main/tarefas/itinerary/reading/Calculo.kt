package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

import br.com.iresult.gmfmobile.BuildConfig
import br.com.iresult.gmfmobile.model.Visita
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.utils.defaultScheduler

class Calculo {

    var ligacao: Ligacao? = null
    var leitura: Int = 0
    private var dataBase: AppDataBase? = null


    constructor(dataBase: AppDataBase, ligacao: Ligacao, leitura: Int) {
        this.dataBase = dataBase
        this.ligacao = ligacao
        this.leitura = leitura
    }

    @Throws(Exception::class)
    fun calculaConta() {

        Thread(Runnable {
            try {

                val visita = Visita(this.dataBase, this.ligacao, leitura.toString())

                visita.calculaConsumo()

                visita.calculaFatura()

                visita.atualizarStatus()

                val imprime = visita.verificaRetencao()

                // Força o consumo Faturado de Água para zero nos casos de T.E.E
                if (ligacao?.codigoFaturamento == 3) {
                    visita.consumoFaturadoAgua = 0
                }

                // Configura os parâmetros de impressão (faixas de consumo e serviços -
                // LIGA00)
                //     if (imprime < 50) {
                visita.parametrosImpressao()
                //     }

                /*
            // Se tiver notificação de débitos, avisa e imprime
            if (FaturamentoDAO.getInstance().verificaFaturamento(
                    ligacao.numeroLigacao)) {
                this.notificacaoDebitos();
            }
            */


                visita.seqLeitura = dataBase?.leituraDao()?.getMaxSeqLeitura()?.blockingFirst()!!

                val impressaoConta = ImpressaoConta()
                impressaoConta.imprimirConta(ligacao, visita, dataBase)
                atualizaLigacao(visita)
                atualiLeitura(visita)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    private fun atualizaLigacao(visita: Visita) {
        ligacao?.statusRegistro = visita.statusRegistro.toString()

        ligacao?.let { dataBase?.ligacaoDao()?.updateLigacao(it) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun atualiLeitura(visita: Visita) {

        val leitura = dataBase?.leituraDao()?.getLeitura(visita.numeroLigacao)?.defaultScheduler()?.blockingFirst()?.get(0)
        if (leitura != null) {

            leitura.leituraAtual = visita.leituraAtual
            leitura.numMat = ligacao?.numeroLigacao!!
            leitura.numMat = java.lang.Long.valueOf(dataBase?.usuarioDao()?.getUsuario()?.blockingGet()?.get(0)?.codigo)
            leitura.codLeitura = visita.codigoLeitura
            leitura.codLeituraInterno = visita.codigoLeituraInterno

            val sdf = SimpleDateFormat("yyyyMMdd")
            leitura.dataLeitura = sdf.format(visita.dataLeitura)
            leitura.horaLeitura = visita.horarioLeitura
            leitura.quantidadeTentativas = visita.quantidadeTentativas
            leitura.consumoMedido = visita.consumoMedido
            leitura.consumoMedidoEE = visita.consumoMedidoEsgoto
            leitura.conusumoFaturado = visita.consumoFaturadoAgua
            leitura.conusumoFaturadoEE = visita.consumoFaturadoEsgoto
            leitura.valorTotal = visita.valorTotal
            leitura.qtdCons = visita.quantidadeDiasConsumo
            leitura.criterioFaturamento = visita.criterioFaturamento
            leitura.descStatus = visita.statusVirada + visita.statusRepasse!!
            leitura.descDica = visita.dica
            leitura.credConsumo = visita.creditoConsumo
            leitura.tipoEntrega = visita.tipoEntrega
            leitura.descMotivoEntrega = visita.motivoEntrega
            leitura.qtdEmissoes = visita.quantidadeEmissao
            leitura.descObs = visita.descricaoObservacao
            leitura.versaoColetor = BuildConfig.VERSION_NAME
            // leitura.setStatusBateria();
            leitura.sqLeitura = visita.seqLeitura
            leitura.valorMinimo = visita.valorMinNaoLancado
            leitura.statusRegistro = visita.statusRegistro
            dataBase?.leituraDao()?.updateLeitura(leitura)
        }
    }
}
