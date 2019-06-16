package br.com.iresult.gmfmobile.repository

import br.com.iresult.gmfmobile.model.bean.*
import br.com.iresult.gmfmobile.model.dao.*
import br.com.iresult.gmfmobile.service.LoginService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class LoginRepository(private val loginService: LoginService,
                      private val usuarioDao: UsuarioDao,
                      private val ligacaoDao: LigacaoDao,
                      private val estatisticaDao: PreenchimentoEstatisticaDao,
                      private val roteiroDao: RoteiroDao,
                      private val parametroImpressaoDao: ParametroImpressaoDao,
                      private val rubricaDao: RubricaDao,
                      private val leituraDao: LeituraDao,
                      private val faixaDao: FaixaDao,
                      private val parametroDao: ParametroDao,
                      private val servicoDao: ServicoDao,
                      private val categoriaDao: CategoriaDao,
                      private val tarifaDao: TarifaDao,
                      private val dominioDao: DominioDao

                      ) {

    fun login(login: String, password: String): Observable<LoginResponse> {
        return loginService.login(login, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun insertUsuario(usuario: Usuario) {
        doAsync { usuarioDao.insert(usuario) }
    }

    fun insertUnidades(ligacoes: List<Ligacao>) {
        doAsync { ligacaoDao.insertUnidades(ligacoes) }
    }

    fun insertEstatistica(estatistica: PreenchimentoEstatistica) {
        doAsync { estatisticaDao.insertEstatistica(estatistica) }
    }

    fun insertRoteiro(roteiro: Roteiro) {
        doAsync { roteiroDao.insert(roteiro) }
    }

    fun insertparametroImpressao(parametroImpressao: ParametroImpressao) {
        doAsync { parametroImpressaoDao.insert(parametroImpressao) }
    }

    fun insertRubrica(rubrica: List<Rubrica>) {
        doAsync { rubricaDao.insertAll(rubrica) }
    }

    fun inserirLeituras(leitura: Leitura){
        doAsync { leituraDao.insert(leitura) }
    }

    fun inserirFaixas(faixas:  List<Faixa>){
        doAsync { faixaDao.insertAll(faixas) }
    }
    fun inserirParametros(faixas:  List<Parametro>){
        doAsync { parametroDao.insertAll(faixas) }
    }

    fun inserirCategorias(categoria:  List<Categoria>){
        doAsync { categoriaDao.insertAll(categoria) }
    }
    fun inserirServicos(servicos:  List<Servico>){
        doAsync { servicoDao.insertAll(servicos) }
    }
    fun inserirTarifas(tarifas:  List<Tarifa>){
        doAsync { tarifaDao.insertAll(tarifas) }
    }
    fun inserirDominios(dominios:  List<Dominio>){
        doAsync { dominioDao.insertAll(dominios) }
    }

}