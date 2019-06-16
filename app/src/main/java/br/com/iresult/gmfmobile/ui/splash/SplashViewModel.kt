package br.com.iresult.gmfmobile.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Usuario
import br.com.iresult.gmfmobile.repository.UserRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.disposedBy

class SplashViewModel(private val userRepository: UserRepository): BaseViewModel<SplashViewModel.State>() {

    private val mUser = MutableLiveData<Usuario?>()
    val user: LiveData<Usuario?> = mUser

    enum class State : BaseState

    fun getUser() {
        userRepository.getUser().subscribe({
            mUser.value = it.firstOrNull()
        },{
            Log.e("SplashViewModel", it.toString())
        }).disposedBy(disposeBag)
    }
}