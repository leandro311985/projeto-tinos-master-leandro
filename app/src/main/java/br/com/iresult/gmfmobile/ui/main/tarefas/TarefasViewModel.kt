package br.com.iresult.gmfmobile.ui.main.tarefas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.repository.RoteiroRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.PreferencesKey
import br.com.iresult.gmfmobile.utils.PreferencesManager
import br.com.iresult.gmfmobile.utils.disposedBy

class TarefasViewModel(roteiroRepository: RoteiroRepository, private val preferencesManager: PreferencesManager) : BaseViewModel<TarefasViewModel.State>() {

    private val mRoteiros = MutableLiveData<List<Roteiro>>()
    val roteiros: LiveData<List<Roteiro>> = mRoteiros

    enum class State : BaseState

    init {
        roteiroRepository.getRoteiros().subscribe({
            mRoteiros.value = it
        }, {
            Log.e("TarefasViewModel", it.toString())
        }).disposedBy(disposeBag)
    }

    fun notShowInformatives(): Boolean {
        return preferencesManager.get(PreferencesKey.NOT_SHOW_INFORMATIVES, false) == true
    }
}