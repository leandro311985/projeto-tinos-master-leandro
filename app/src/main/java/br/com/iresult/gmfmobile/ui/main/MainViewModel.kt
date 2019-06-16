package br.com.iresult.gmfmobile.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Usuario
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.PreferencesManager
import br.com.iresult.gmfmobile.utils.defaultScheduler
import br.com.iresult.gmfmobile.utils.disposedBy
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by victorfernandes on 23/02/19.
 */
class MainViewModel(private val dataBase: AppDataBase,
                    private val preferencesManager: PreferencesManager) : BaseViewModel<MainViewModel.State>() {

    enum class State : BaseState {
        LOGOUT
    }

    private val usuario = MutableLiveData<Usuario>()

    init {
        dataBase.usuarioDao()
                .getUsuario()
                .defaultScheduler()
                .subscribe({
                    usuario.value = it.firstOrNull()
                }, {
                    Log.e("MainViewModel", it.toString())
                }).disposedBy(disposeBag)
    }

    fun getUsuario(): LiveData<Usuario> = usuario

    fun logout() {

        doAsync {

            dataBase.clearAllTables()
            preferencesManager.clear()

            uiThread {
                Log.i("MainViewModel", "all tables was cleared")
                setState(State.LOGOUT)
            }
        }
    }
}