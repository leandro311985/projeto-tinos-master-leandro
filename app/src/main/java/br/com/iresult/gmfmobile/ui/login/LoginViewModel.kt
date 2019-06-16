package br.com.iresult.gmfmobile.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.iresult.gmfmobile.model.bean.*
import br.com.iresult.gmfmobile.repository.LoginRepository
import br.com.iresult.gmfmobile.ui.base.BaseViewModel
import br.com.iresult.gmfmobile.utils.disposedBy

/**
 * Created by victorfernandes on 17/02/19.
 */

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel<LoginViewModel.State>() {

    enum class State : BaseState {
        LOADING,
        SUCCESS,
        ERROR
    }

    private val usuario = MutableLiveData<String>()
    private val senha = MutableLiveData<String>()
    private val loginData = MutableLiveData<LoginResponse>()

    fun doLogin() {

        val login = usuario.value ?: return
        val password = senha.value ?: return

        setState(State.LOADING)
        loginRepository.login(login, password).subscribe({ response ->

            loginRepository.insertUsuario(response.usuario)
            response.roteiros?.forEach {roteiro ->
                roteiro.porcentagem = 0.toDouble()
                roteiro.totalRuas = roteiro.tarefas?.addresses?.count()?.toLong()
                roteiro.totalCasas = 0

                var countCasas = 0

                roteiro.tarefas?.addresses?.forEach { rua ->
                    countCasas += rua.ligacoes?.count()!!

                    val proximaLigacao = if(rua.ligacoes.count() > 1) {
                        rua.ligacoes[1]
                    } else {
                        rua.ligacoes.first()
                    }

                    val estatistica = PreenchimentoEstatistica(
                            roteiro.nome,
                            rua.nome,
                            rua.ligacoes.first(),
                            proximaLigacao
                    )

                    loginRepository.insertUnidades(rua.ligacoes)
                    loginRepository.insertEstatistica(estatistica)
                    roteiro.parametroImpressao?.let { loginRepository.insertparametroImpressao(it) }

                }

                roteiro.leitura?.forEach { leitura ->
                    loginRepository.inserirLeituras(leitura)
                };
                roteiro.parametroImpressao?.let { loginRepository.insertparametroImpressao(it) };
                roteiro.faixas?.let { loginRepository.inserirFaixas(it) };
                roteiro.parametros.let {
                    if (it != null) {
                        loginRepository.inserirParametros(it)
                    }
                };
                roteiro.categorias?.let { loginRepository.inserirCategorias(it) };
                roteiro.servicos?.let { loginRepository.inserirServicos(it) };
                roteiro.tarifas?.let { loginRepository.inserirTarifas(it) };
                roteiro.dominios?.let { loginRepository.inserirDominios(it) };

                roteiro.rubricas?.let { loginRepository.insertRubrica(it) };
                roteiro.totalCasas = countCasas.toLong()

                roteiro.faixas = ArrayList<Faixa>()
                roteiro.categorias = ArrayList<Categoria>()
                roteiro.servicos = ArrayList<Servico>()
                roteiro.dominios = ArrayList<Dominio>()
                roteiro.rubricas = ArrayList<Rubrica>()


                loginRepository.insertRoteiro(roteiro)

            }
            loginData.value = response
            setState(State.SUCCESS)
        },{
            setState(State.ERROR)
            it.printStackTrace()
        }).disposedBy(disposeBag)
    }

    fun setUsuarioValue(value: String) {
        if(!usuario.value.equals(value)) {
            usuario.value = value
        }
    }

    fun setSenhaValue(value: String) {
        if(!senha.value.equals(value)) {
            senha.value = value
        }
    }

    fun getUsuario() : LiveData<String> = usuario
    fun getSenha() : LiveData<String> = senha
    fun getData() : LiveData<LoginResponse> = loginData

}