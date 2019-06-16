package br.com.iresult.gmfmobile.ui.main.impressao.ultimos

import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.ui.main.impressao.Impressao

class ImpressaoUltimosViewModel(val dataBase: AppDataBase) : BaseViewModel<ImpressaoUltimosViewModel.State>()  {

    val impressao: Impressao = Impressao(dataBase)

    enum class State : BaseState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun imprimeUltimos50(): Boolean{
        return false // impressao.imprimir50()
    }
}