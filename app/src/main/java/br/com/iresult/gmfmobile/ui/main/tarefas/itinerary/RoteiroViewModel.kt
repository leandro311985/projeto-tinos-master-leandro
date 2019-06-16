package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.repository.RoteiroRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.utils.disposedBy

/**
 * Created by victorfernandes on 24/02/19.
 */

class RoteiroViewModel(private val repository: RoteiroRepository,  val dataBase: AppDataBase) : BaseViewModel<RoteiroViewModel.State>() {

    enum class State : BaseState

    private val mRoteiro = MutableLiveData<Roteiro>()
    val roteiro: LiveData<Roteiro> = mRoteiro

    private val mAddress = MutableLiveData<List<Address>>()
    val address: LiveData<List<Address>> = mAddress

    private val mLigacao = MutableLiveData<List<Ligacao>>()
    val ligacao: LiveData<List<Ligacao>> = mLigacao

    private val mProgress = MutableLiveData<Int>()
    val progess: LiveData<Int> = mProgress

    val mErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mErrorMessage

    fun fetchAdressesByFileName(nome: String) {
        repository.getRoteiro(nome).subscribe({
            mRoteiro.value = it
            it.tarefas?.addresses?.let { ruas ->
                repository.insertRuas(ruas)
            }
            mAddress.value = it.tarefas?.addresses
            this.fetchProgressTask()
        }, {
            Log.e("RoteiroViewModel", it.toString())
        }).disposedBy(disposeBag)
    }

    private fun fetchProgressTask() {
        repository.allImoveis().subscribe({ list ->
            val totalDone = list.filter { it.filterDone() }.size.toFloat()
            val percentage = (totalDone / list.size.toFloat())
            mProgress.value = (percentage * 100).toInt()
        }, {
            Log.e("RoteiroViewModel", it.toString())
        }).disposedBy(disposeBag)
    }

    fun fetchHouses(streetName: String) {
        mLigacao.value = emptyList()
        repository.fetchImoveis(streetName).subscribe({
            mLigacao.value = it
        }, {
            Log.e("RoteiroViewModel", it.toString())
        }).disposedBy(disposeBag)
    }

    private fun Ligacao.filterDone(): Boolean {
        return this.status != null
                && this.status != HomeStatus.AGORA.name
                && this.status != HomeStatus.PROXIMO.name
    }

    private fun Ligacao.filterNotDone(): Boolean {
        return this.status == null
                || this.status == HomeStatus.AGORA.name
                || this.status == HomeStatus.PROXIMO.name
    }

    fun formattedHouses(ligacoes: List<Ligacao>?, pedding: Boolean, finished: Boolean, reversed: Boolean): List<Ligacao> {
        val worked = ligacoes?.filter { it.filterDone() } ?: emptyList()
        val notWorked = ligacoes?.filter { it.filterNotDone() } ?: emptyList()
        notWorked.firstOrNull()?.let {
            it.status = HomeStatus.AGORA.name
        }
        if (notWorked.size >= 2) {
            notWorked[1].status = HomeStatus.PROXIMO.name
        }
        val completeHouses = worked + notWorked
        val filteredPendding = completeHouses.filter { it.filterNotDone() }
        val filteredFinished = completeHouses.filter { it.filterDone() }

        var finalHouses = mutableListOf<Ligacao>()
        if (!pedding && !finished) {
            finalHouses.addAll(filteredFinished)
            finalHouses.addAll(filteredPendding)
        }

        if (finished) {
            finalHouses.addAll(filteredFinished)
        }

        if (pedding) {
            finalHouses.addAll(filteredPendding)
        }
        finalHouses = finalHouses.sortedBy { it.numReg }.toMutableList()
        if (reversed) {
            finalHouses = finalHouses.asReversed()
        }
        return finalHouses
    }
}