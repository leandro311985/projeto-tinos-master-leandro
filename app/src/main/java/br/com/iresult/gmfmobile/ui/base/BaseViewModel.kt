package br.com.iresult.gmfmobile.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<S : BaseViewModel.BaseState> : ViewModel() {

    interface BaseState

    private val liveState = MutableLiveData<S>()

    protected val disposeBag = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }

    fun getState(): LiveData<S> = liveState
    fun setState(state: S?) {
        liveState.postValue(state)
    }

    open fun clearState() {
        setState(null)
    }
}