package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.bean.LigacaoWrapper
import br.com.iresult.gmfmobile.repository.GeocodingRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.disposedBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapHomeViewModel(private val geocodingRepository: GeocodingRepository) : BaseViewModel<MapHomeViewModel.State>() {

    enum class State : BaseState {
        LOADING,
        SUCCESS,
        ERROR
    }

    private lateinit var googleMapApiKey: String

    private val mAddress = MutableLiveData<Ligacao>()
    val address: LiveData<Ligacao> = mAddress

    fun initViewModel(googleMapApiKey: String) {
        this.googleMapApiKey = googleMapApiKey
    }

    fun geocodeAddress(locationName: String, ligacao: Ligacao) {
        setState(State.LOADING)
        return geocodingRepository.geocode(locationName, googleMapApiKey)
                .subscribeOn(Schedulers.io())
                .map { it.results?.firstOrNull() }
                .map {
                    LigacaoWrapper(ligacao.copy(latitude = it.geometry?.location?.lat, longitude = it.geometry?.location?.lng))
                }
                .onErrorResumeNext {
                    Single.just(LigacaoWrapper(null))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setState(State.SUCCESS)
                    mAddress.value = it.ligacao
                }, { error ->
                    Log.e("MapHomeViewModel", "geocodeAddress", error)
                    setState(State.ERROR)
                })
                .disposedBy(disposeBag)
    }
}