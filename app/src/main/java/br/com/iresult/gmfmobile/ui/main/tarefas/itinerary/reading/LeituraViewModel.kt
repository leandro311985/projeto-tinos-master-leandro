package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.Visita
import br.com.iresult.gmfmobile.model.bean.*
import br.com.iresult.gmfmobile.print.ZebraConnection
import br.com.iresult.gmfmobile.repository.LeituraRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.ui.base.FormaEntregaDialog
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.utils.defaultScheduler
import br.com.iresult.gmfmobile.utils.disposedBy
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.lang.Exception

/**
 * Created by victorfernandes on 24/02/19.
 */
class LeituraViewModel(val leituraRepository: LeituraRepository) : BaseViewModel<LeituraViewModel.State>() {

    enum class State : BaseState {
        OCORRENCIA_IMPEDITIVA,
        IMPRESSAO,
        ENTREGA
    }

    private val habilitarBotaoEnvio = MutableLiveData<Boolean>()
    private val habilitarBotaoOcorrencia = MutableLiveData<Boolean>()

    val mUnidade = MutableLiveData<Ligacao>()
    val unidade: LiveData<Ligacao> = mUnidade

    private val mRua = MutableLiveData<Address>()
    val address: LiveData<Address> = mRua

    private val mOcorrencia = MutableLiveData<OcorrenciaLeitura>()
    val ocorrencia: LiveData<OcorrenciaLeitura> = mOcorrencia

    //TODO Leitura
    private val mLeitura = MutableLiveData<String>()
    val leitura: LiveData<String> = mLeitura

    //TODO Ocorrencia
    private val mTiposOcorrencia = MutableLiveData<List<Ocorrencia>>()
    val tiposOcorrencia: LiveData<List<Ocorrencia>> = mTiposOcorrencia

    private val mTipoOcorrenciaSelecionada = MutableLiveData<Ocorrencia?>()
    val tipoOcorrenciaSelecionada: LiveData<Ocorrencia?> = mTipoOcorrenciaSelecionada

    private val mJustificativaSelecionada = MutableLiveData<Justificativa?>()
    val justificativaSelecionada: LiveData<Justificativa?> = mJustificativaSelecionada

    private val mObservacaoOcorrencia = MutableLiveData<String>()
    val observacaoOcorrencia: LiveData<String> = mObservacaoOcorrencia

    private val mImagensOcorrencia = MutableLiveData<MutableList<File>>()
    val imagensOcorrencia: LiveData<MutableList<File>> = mImagensOcorrencia

    private val justificativas = MutableLiveData<List<Justificativa>>()

    private val mJustificativasOcorrencia = MutableLiveData<List<Justificativa>>()
    val justificativasOcorrencia: LiveData<List<Justificativa>> = mJustificativasOcorrencia

    private val mEnableDisableJustify = MutableLiveData<Boolean>()
    val enableDisableJustify: LiveData<Boolean> = mEnableDisableJustify

    private val mHabilitarCampoTexto = MutableLiveData<Boolean>()
    val isHabilitarCampoTexto: LiveData<Boolean> = mHabilitarCampoTexto


    //TODO ORDEM SERVICO
    private val mServicos = MutableLiveData<List<Rubrica>>()
    val servicos: LiveData<List<Rubrica>> = mServicos

    private val mServicoSelecionado = MutableLiveData<Rubrica>()
    val servicoSelecionado: LiveData<Rubrica> = mServicoSelecionado

    private val mTextoServico = MutableLiveData<String>()
    val textoServico: LiveData<String> = mTextoServico

    private val mImagensServico = MutableLiveData<MutableList<File>>()
    val imagensServico: LiveData<MutableList<File>> = mImagensServico

    private val mServicosLeituras = MutableLiveData<List<ServicoLeitura>>()
    val servicosLeitura: LiveData<List<ServicoLeitura>> = mServicosLeituras

    private val mErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mErrorMessage

    private val mErroLeitura = MutableLiveData<String>()
    val erroLeitura: LiveData<String> = mErroLeitura


    private val mUpdatedUnidade = MutableLiveData<Ligacao>()
    val updatedUnidade: LiveData<Ligacao> = mUpdatedUnidade

    init {
        habilitarBotaoEnvio.value = true
        habilitarBotaoOcorrencia.value = true
        mLeitura.value = ""
    }

    fun initModel(address: Address, ligacao: Ligacao, roteiro: String) {
        this.mRua.value = address
        this.mUnidade.value = ligacao

        leituraRepository.dataBase
                .roteiroDao()
                .getRoteiro(roteiro)
                .defaultScheduler()
                .subscribe({
                    mTiposOcorrencia.value = it.ocorrencias
                    justificativas.value = it.justificativas
                    mServicos.value = it.rubricas
                }, {
                    Log.e("LeituraViewModel", it.toString())
                }).disposedBy(disposeBag)

        leituraRepository.dataBase
                .servicoDao()
                .getServicosLeitura(ligacao.numeroLigacao)
                .defaultScheduler()
                .subscribe({
                    mServicosLeituras.value = it
                }, {
                    Log.e("LeituraViewModel", it.toString())
                }).disposedBy(disposeBag)
    }

    fun formatedOccourenceList(): Array<String> {
        return tiposOcorrencia.value?.map {
            "${it.codigo} - ${it.descricao}"

        }?.toTypedArray() ?: emptyArray()
    }

    fun formatedJustifyList(): Array<String> {
        return justificativasOcorrencia.value?.map {
            it.descricao ?: "--------------"
        }?.toTypedArray() ?: emptyArray()
    }

    fun habilitarBotaoOcorrencia() = habilitarBotaoOcorrencia

    fun setLeitura(valor: String) {

        if (!mLeitura.value.equals(valor)) {
            mLeitura.value = valor
        }
    }

    fun statusRegistroDescription(code: String) = when (code) {
        "1" -> "Ativo"
        "2" -> "Cortada"
        "3" -> "Corte a pedido"
        else -> "Não informado"
    }

    fun deveHabilitarBotaoEnvio() = habilitarBotaoEnvio

    fun selecionaTipoOcorrencia(posicao: Int) {

        mTipoOcorrenciaSelecionada.value = mTiposOcorrencia.value?.get(posicao)
        val tipoOcorrencia = mTipoOcorrenciaSelecionada.value ?: return

        if (mTipoOcorrenciaSelecionada.value?.statusJustificativa == Ocorrencia.STATUS_JUSTIFICADO) {

            val justificativas = justificativas.value?.filter {
                it.codigo == tipoOcorrencia.codigo
            }

            mJustificativasOcorrencia.value = justificativas
            mEnableDisableJustify.value = true
        } else {
            mEnableDisableJustify.value = false
        }
    }

    fun selecionaJustificativa(posicao: Int) {

        mJustificativaSelecionada.value = mJustificativasOcorrencia.value?.get(posicao)
        mHabilitarCampoTexto.value = justificativaSelecionada.value?.descricao?.contains("texto", true)

    }

    fun salvarOcorrencia() {

        val numeroLigacao = mUnidade.value?.numeroLigacao ?: return
        val tipoOcorrenciaSelecionada = mTipoOcorrenciaSelecionada.value ?: return
        val ocorrencia = OcorrenciaLeitura(0, numeroLigacao, tipoOcorrenciaSelecionada, mImagensOcorrencia.value)

        doAsync {
            leituraRepository.dataBase.ocorrenciaLeituraDao().insertOcorrencia(ocorrencia)

            uiThread {
                mOcorrencia.value = ocorrencia
                habilitarBotaoOcorrencia.value = false
                mHabilitarCampoTexto.value = !tipoOcorrenciaSelecionada.isOcorrenciaImpeditiva()
            }
        }
    }

    /**
     * Limpa as informacoes de ocorrencia
     */
    fun limparOcorrencia() {
        mTipoOcorrenciaSelecionada.value = null
        mJustificativaSelecionada.value = null
        mObservacaoOcorrencia.value = null
    }

    fun getFormattedAddress(compl: String?): String {
        val address = address.value
        return "${address?.nome}\n" +
                "Compl. $compl\n" +
                "${address?.bairro} ${address?.cep}"
    }

    fun observacaoOcorrenciaValueChanged(value: String) {
        mObservacaoOcorrencia.value = value
    }

    fun addImagemOcorrencia(file: File) {
        if (mImagensOcorrencia.value == null) {
            mImagensOcorrencia.value = ArrayList()
        }
        mImagensOcorrencia.value?.add(file)
        mImagensOcorrencia.postValue(mImagensOcorrencia.value)
    }

    fun removeImageOccourence(file: File) {
        mImagensOcorrencia.value?.remove(file)
        mImagensOcorrencia.value = mImagensOcorrencia.value
    }

    fun addImagemServico(file: File) {
        if (mImagensServico.value == null) {
            mImagensServico.value = ArrayList()
        }
        mImagensServico.value?.add(file)
        mImagensServico.postValue(mImagensServico.value)

    }

    fun removeImageServiceOrder(file: File) {
        mImagensServico.value?.remove(file)
        mImagensServico.value = mImagensServico.value
    }

    fun selecionarServico(servico: Int) {
        mServicoSelecionado.value = mServicos.value?.get(servico)
    }

    fun onTextoServicoChanged(value: String) {
        if (!mTextoServico.value.equals(value)) {
            mTextoServico.value = value
        }
    }

    fun salvarServico() {
        val numeroLigacao = mUnidade.value?.numeroLigacao ?: return
        val servicoSelecionado = mServicoSelecionado.value ?: return
        val servico = ServicoLeitura(0, numeroLigacao, mTextoServico.value, servicoSelecionado,
                mImagensServico.value
        )

        doAsync { leituraRepository.dataBase.servicoDao().insert(servico) }

        mTextoServico.value = null
        mServicoSelecionado.value = null
        leituraRepository.dataBase
                .servicoDao()
                .getServicosLeitura(numeroLigacao)
                .defaultScheduler()
                .subscribe({
                    mServicosLeituras.value = it
                }, {
                    Log.e("LeituraViewModel", it.toString())
                }).disposedBy(disposeBag)
    }

    fun removerServico(servicoLeitura: ServicoLeitura) {
        doAsync { leituraRepository.dataBase.servicoDao().delete(servicoLeitura) }
        val numeroLigacao = mUnidade.value?.numeroLigacao ?: return
        leituraRepository.dataBase
                .servicoDao()
                .getServicosLeitura(numeroLigacao)
                .defaultScheduler()
                .subscribe({
                    mServicosLeituras.value = it
                }, {
                    Log.e("LeituraViewModel", it.toString())
                }).disposedBy(disposeBag)
    }

    fun atualizarServicos() {
        val numeroLigacao = mUnidade.value?.numeroLigacao ?: return
        leituraRepository.dataBase
                .servicoDao()
                .getServicosLeitura(numeroLigacao)
                .defaultScheduler()
                .subscribe({
                    mServicosLeituras.value = it
                }, {
                    Log.e("LeituraViewModel", it.toString())
                }).disposedBy(disposeBag)
    }

    fun printInvoice(qtdTentativas: Int, visita: Visita) {

        mLeitura.value?.let { leit ->

            when {
                StringUtils.isBlank(leit) -> {

                    mErroLeitura.value = "LEITURA NÃO INFORMADA. PROSSEGUE?"
                    return

                }
                leit.count() > 6 -> {

                    mErrorMessage.value = "ATENÇÃO: REVISE A LEITURA"
                    return

                }

                // consumo igual ao último
                leit.toInt() == visita.ligacao?.leituraAnteriorInt!! ->{

                    mErrorMessage.value = "Consumo Zero (0)"
                    return
                }

                // consumo igual ao último
                visita.ligacao?.leituraMaxima!! > 0 &&
                        leit.toInt() < visita.ligacao?.leituraAnteriorInt!! ->{

                    mErrorMessage.value = "Atenção... Confirme a Leitura"
                    return
                }

                // consumo igual ao último
                visita.ligacao?.leituraMaxima!! > 0 &&
                        leit.toInt() > visita.ligacao?.leituraMaxima!! ->{

                    mErrorMessage.value = "Leitura maior que número de ponteiros, Confirme"
                    return
                }

                leit.toInt() < (visita.ligacao?.leituraAnteriorInt!! + visita.ligacao?.consumoMinimoB!! ) ->{

                    mErrorMessage.value = "Fora de Faixa, Confirme."
                    return
                }

                leit.toInt() > (visita.ligacao?.leituraAnteriorInt!! + visita.ligacao?.consumoMaximoB!! ) ->{

                    mErrorMessage.value = "Fora de Faixa, Confirme."
                    return
                }

                else -> {
                    if (!isImpressoraConectada()){
                        mErrorMessage.value = "Impressora não configrada corretamente, favor verificar."
                        return
                    }
                    setState(State.IMPRESSAO)
                }
            }
        }
    }

    fun isImpressoraConectada() : Boolean {
        try {
            return (ZebraConnection()).isPortOpen()
        } catch ( e : Exception) {
            return false;
        }
        return false
    }

    fun saveUnidade(statusEntrega: FormaEntregaDialog.FormaEntrega) {
        mUnidade.value?.let { ligacao ->

            ligacao.status = when (statusEntrega) {
                FormaEntregaDialog.FormaEntrega.FORMA_ENTREGA_NAO_ENTREGUE -> HomeStatus.NAO_ENTREGUE.name
                else -> HomeStatus.CONCLUIDO.name
            }
            doAsync {
                leituraRepository.dataBase.ligacaoDao().updateLigacao(ligacao).subscribe({ numberOfRows ->

                    uiThread {
                        mUpdatedUnidade.value = ligacao
                    }

                }, {
                    Log.e("LeituraViewModel", "Error to update $it")
                }).disposedBy(disposeBag)
            }
        }
    }
}