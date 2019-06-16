package br.com.iresult.gmfmobile.ui.main.pesquisa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.repository.PesquisaRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.disposedBy

class PesquisaViewModel(private val pesquisaRepository: PesquisaRepository) : BaseViewModel<PesquisaViewModel.State>() {

    enum class State : BaseState {
        READY,
        LOADING,
        SUCCESS,
        ERROR
    }

    enum class SearchType(val title: String) {
        MEDIDOR("Medidor"),
        MATRICULA("Matrícula"),
        NOME("Nome"),
        ENDERECO("Endereço")
    }

    private val searchTerm = MutableLiveData<String>()
    private val searchType = MutableLiveData<SearchType>()
    private val resultados = MutableLiveData<List<Ligacao>>()

    fun getSearchTerm(): LiveData<String> = searchTerm
    fun getSearchType(): LiveData<SearchType> = searchType
    fun getResultados(): LiveData<List<Ligacao>> = resultados

    init {
        checkFieldsReady()
    }

    fun initModelForSearch() {
        searchType.value = null
        searchTerm.value = null
        resultados.value = null
    }

    fun initModelForResult(type: SearchType, term: String) {
        searchType.value = type
        searchTerm.value = term
        resultados.value = null
        search()
    }

    fun search() {
        searchType.value?.let { type ->
            searchTerm.value?.let { search ->
                when (type) {
                    SearchType.MEDIDOR -> {
                        pesquisaRepository.searchByMedidor(search).subscribe({
                            resultados.value = it
                        }, {

                        }).disposedBy(disposeBag)
                    }
                    SearchType.MATRICULA -> {
                        pesquisaRepository.searchByMatricula(search).subscribe({
                            resultados.value = it
                        }, {

                        }).disposedBy(disposeBag)
                    }
                    SearchType.NOME -> {
                        pesquisaRepository.searchByNome(search).subscribe({
                            resultados.value = it
                        }, {

                        }).disposedBy(disposeBag)
                    }
                    SearchType.ENDERECO -> {
                        pesquisaRepository.searchByEndereco(search).subscribe({
                            resultados.value = it
                        }, {

                        }).disposedBy(disposeBag)
                    }
                }
                setState(State.SUCCESS)
            }
        }
    }

    fun setSearchTermValue(value: String) {
        if (!searchTerm.value.equals(value)) {
            searchTerm.value = value
        }
        checkFieldsReady()
    }

    fun setSearchTypeValue(value: SearchType) {
        if (searchType.value != value) {
            searchType.value = value
        }
        checkFieldsReady()
    }

    private fun checkFieldsReady() {
        if (searchType.value != null && searchTerm.value != null && searchTerm.value?.length!! >= 3) {
            setState(State.READY)
        } else {
            setState(null)
        }
    }

    private fun getMockedResultados(): List<Ligacao> {

        val list = mutableListOf<Ligacao>()
/*
        val resultado1 = Ligacao(
                "123123",
                123123,
                null,
                null,
                "Address Francisco Isoldi",
                "226",
                "Sumarezinho",
                "São Paulo",
                "SP",
                "005040-140",
                "Ap 81B",
                "Alexandre Prado",
                null,
                "123ASD",
                null,
                null,
                null,
                null,
                null,
                "",
                0,
                0,
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                0,
                0,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                null,
                null,
                0,
                null,
                true, "",
                null,
                null
        )

        val resultado2 = Ligacao(
                "456456",
                123123,
                null,
                null,
                "Address Natingui",
                "862",
                "Vila Madalena",
                "São Paulo",
                "SP",
                null,
                null,
                "Thomas T",
                null,
                "876GSU",
                null,
                null,
                null,
                null,
                null,
                "",
                0,
                0,
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                0,
                0,
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                null,
                null,
                0,
                null,
                true, "",
                null,
                null
        )

        list.add(resultado1)
        list.add(resultado2)*/
        return list
    }
}