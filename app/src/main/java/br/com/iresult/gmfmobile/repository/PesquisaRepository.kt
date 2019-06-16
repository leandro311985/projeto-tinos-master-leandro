package br.com.iresult.gmfmobile.repository

import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.dao.LigacaoDao
import br.com.iresult.gmfmobile.utils.defaultScheduler
import io.reactivex.Observable

class PesquisaRepository(private val ligacaoDao: LigacaoDao) {


    fun searchByMedidor(query: String): Observable<List<Ligacao>> {
        return ligacaoDao.searchByMedidor(query).defaultScheduler()
    }

    fun searchByMatricula(query: String): Observable<List<Ligacao>> {
        return ligacaoDao.searchByMatricula(query).defaultScheduler()
    }

    fun searchByNome(query: String): Observable<List<Ligacao>> {
        return ligacaoDao.searchByNome(query).defaultScheduler()
    }

    fun searchByEndereco(query: String): Observable<List<Ligacao>> {
        return ligacaoDao.searchByEndereco(query).defaultScheduler()
    }
}