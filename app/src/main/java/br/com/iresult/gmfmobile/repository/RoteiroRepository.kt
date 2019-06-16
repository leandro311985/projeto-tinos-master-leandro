package br.com.iresult.gmfmobile.repository

import br.com.iresult.gmfmobile.model.bean.PreenchimentoEstatistica
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.dao.PreenchimentoEstatisticaDao
import br.com.iresult.gmfmobile.model.dao.RoteiroDao
import br.com.iresult.gmfmobile.model.dao.AddressDao
import br.com.iresult.gmfmobile.model.dao.LigacaoDao
import br.com.iresult.gmfmobile.utils.defaultScheduler
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class RoteiroRepository(private val estatisticaDao: PreenchimentoEstatisticaDao,
                        private val roteiroDao: RoteiroDao,
                        private val addressDao: AddressDao,
                        private val ligacaoDao: LigacaoDao) {

    fun getEstatistica(name: String): Observable<PreenchimentoEstatistica> {
        return estatisticaDao.getEstatistica(name).defaultScheduler()
    }

    fun getRoteiro(name: String): Observable<Roteiro> {
        return roteiroDao.getRoteiro(name).defaultScheduler()
    }

    fun getRoteiros(): Observable<List<Roteiro>> {
        return roteiroDao.getRoteiros().defaultScheduler()
    }

    fun insertRuas(addresses: List<Address>) {
        doAsync { addressDao.insertAll(addresses) }
    }

    fun getEstatisticaByRua(nome: String): Observable<PreenchimentoEstatistica> {
        return estatisticaDao.getEstatisticaByRua(nome).defaultScheduler()
    }

    fun fetchImoveis(streetName: String): Observable<List<Ligacao>> {
        return ligacaoDao.searchByEndereco(streetName).defaultScheduler()
    }

    fun allImoveis(): Observable<List<Ligacao>> {
        return ligacaoDao.all().defaultScheduler()
    }
}