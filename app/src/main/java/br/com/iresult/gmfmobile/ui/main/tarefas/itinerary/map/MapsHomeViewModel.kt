package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.*
import br.com.iresult.gmfmobile.repository.GeocodingRepository
import br.com.iresult.gmfmobile.repository.RoteiroRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.utils.disposedBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class MapsHomeViewModel(private val repository: RoteiroRepository,
                        private val geocodingRepository: GeocodingRepository)
    : BaseViewModel<MapsHomeViewModel.State>() {

    private lateinit var googleMapApiKey: String

    private val mRoteiro = MutableLiveData<Roteiro>()
    val roteiro: LiveData<Roteiro> = mRoteiro

    private val mSelectedAddress = MutableLiveData<Address>()
    val selectedAddress: LiveData<Address> = mSelectedAddress

    private val mAddress = MutableLiveData<List<Address>>()
    val address: LiveData<List<Address>> = mAddress

    private val mStreets = MutableLiveData<List<Pair<Address?, Address?>>>()
    val streets: LiveData<List<Pair<Address?, Address?>>> = mStreets

    private val mLigacao = MutableLiveData<List<Ligacao>>()
    val ligacao: LiveData<List<Ligacao>> = mLigacao

    enum class State : BaseState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun setSelectedAddress(address: Address) {
        if (address != selectedAddress.value) {
            mSelectedAddress.value = address
        }
    }

    fun initViewModel(googleMapApiKey: String) {
        this.googleMapApiKey = googleMapApiKey
    }

    fun fetchAdressesByFileName(nome: String) {
        repository.getRoteiro(nome).subscribe({
            mRoteiro.value = it
            it.tarefas?.addresses?.let { ruas ->
                repository.insertRuas(ruas)
            }

            mAddress.value = it.tarefas?.addresses
            mAddress.value?.let { addressList ->
                fetchStreetsLocation(addressList)
                        .subscribe({ streets ->
                            setState(State.SUCCESS)
                            mStreets.postValue(streets)
                        }, { error ->
                            Log.e("fetchAdresses", "Erro", error)
                            setState(State.ERROR)
                        })
                        .disposedBy(disposeBag)
            }

        }, {
            Log.e("MapsHomeViewModel", it.toString())
        }).disposedBy(disposeBag)
    }

    private fun getStartingAddressInStreet(streetName: String): String? {

        return mAddress.value
                ?.filter { address -> address.nome == streetName }
                ?.map { address -> address.ligacoes?.sortedBy { it.numero?.toInt() }?.firstOrNull() }
                ?.sortedBy { it?.numero?.toInt() }
                ?.firstOrNull()
                ?.let { it.getCompleteAddress(false) }
    }

    private fun getEndingAddressInStreet(streetName: String): String? {

        return mAddress.value
                ?.filter { address -> address.nome == streetName }
                ?.map { address -> address.ligacoes?.sortedByDescending { it.numero?.toInt() }?.firstOrNull() }
                ?.sortedByDescending { it?.numero?.toInt() }
                ?.firstOrNull()
                ?.let { it.getCompleteAddress(false) }
    }

    fun fetchHouses(streetName: String) {
        mLigacao.value = emptyList()
        repository.fetchImoveis(streetName).subscribe({
            fetchHousesLocation(it)
                    .subscribe({ ligacoes ->
                        setState(State.SUCCESS)
                        mLigacao.postValue(ligacoes)
                    }, { error ->
                        Log.e("fetchHousesLocation", "Erro", error)
                        setState(State.ERROR)
                    })
                    .disposedBy(disposeBag)

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

    fun formattedHouses(ligacoes: List<Ligacao>?, pending: Boolean, finished: Boolean, reversed: Boolean): List<Ligacao> {
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
        if (!pending && !finished) {
            finalHouses.addAll(filteredFinished)
            finalHouses.addAll(filteredPendding)
        }

        if (finished) {
            finalHouses.addAll(filteredFinished)
        }

        if (pending) {
            finalHouses.addAll(filteredPendding)
        }
        finalHouses = finalHouses.sortedBy { it.numReg }.toMutableList()
        if (reversed) {
            finalHouses = finalHouses.asReversed()
        }
        return finalHouses
    }

    private fun geocodeAddress(locationName: String, address: Address): Single<AddressWrapper> {
        return geocodingRepository.geocode(locationName, googleMapApiKey)
                .subscribeOn(Schedulers.io())
                .map { it.results?.firstOrNull() }
                .map {
                    AddressWrapper(address.copy(latitude = it.geometry?.location?.lat, longitude = it.geometry?.location?.lng))
                }
                .onErrorResumeNext {
                    Single.just(AddressWrapper(null))
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun geocodeLigacao(locationName: String, ligacao: Ligacao): Single<LigacaoWrapper> {
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
    }

    private fun fetchStreetsLocation(addresses: List<Address>): Single<List<Pair<Address?, Address?>>> {

        setState(State.LOADING)

        val geocoding = mutableListOf<Single<AddressWrapper>>()

        addresses.forEach { address ->
            getStartingAddressInStreet(address.nome)?.let { geocoding.add(geocodeAddress(it, address)) }
            getEndingAddressInStreet(address.nome)?.let { geocoding.add(geocodeAddress(it, address)) }
        }

        return Single.zip(
                geocoding,
                Function { addressList: Array<Any> ->

                    val locations = mutableListOf<Address>()

                    addressList.forEach {
                        if (it is AddressWrapper) {
                            it.address?.let { address -> locations.add(address) }
                        }
                    }

                    return@Function locations
                            .groupBy { address -> address.nome }
                            .entries.map { Pair(it.value.firstOrNull(), it.value.lastOrNull()) }
                })
    }

    private fun fetchHousesLocation(ligacoes: List<Ligacao>): Single<List<Ligacao>> {

        setState(State.LOADING)

        val geocoding = mutableListOf<Single<LigacaoWrapper>>()

        ligacoes.forEach { ligacao ->
            geocoding.add(geocodeLigacao(ligacao.getCompleteAddress(), ligacao))
        }

        return Single.zip(
                geocoding,
                Function { addressList: Array<Any> ->

                    val locations = mutableListOf<Ligacao>()

                    addressList.forEach {
                        if (it is LigacaoWrapper) {
                            it.ligacao?.let { ligacao -> locations.add(ligacao) }
                        }
                    }

                    return@Function locations
                })
    }
}