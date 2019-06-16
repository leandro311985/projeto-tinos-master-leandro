package br.com.iresult.gmfmobile.ui.main.tarefas.informative

import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.PreferencesKey
import br.com.iresult.gmfmobile.utils.PreferencesManager

class InformativosViewModel(private val preferencesManager: PreferencesManager) : BaseViewModel<InformativosViewModel.State>() {

    enum class State : BaseState

    fun verifyNotShowAgain(notShowAgain: Boolean) {
        preferencesManager.set(PreferencesKey.NOT_SHOW_INFORMATIVES, notShowAgain)
    }
}