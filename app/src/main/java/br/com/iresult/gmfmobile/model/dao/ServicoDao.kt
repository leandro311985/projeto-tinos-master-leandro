package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import br.com.iresult.gmfmobile.model.bean.Servico
import br.com.iresult.gmfmobile.model.bean.ServicoLeitura
import io.reactivex.Observable

@Dao
interface ServicoDao {

    @Query("SELECT * FROM ServicoLeitura WHERE numeroLigacao = :numero")
    fun getServicosLeitura(numero: Long): Observable<List<ServicoLeitura>>

    @Query("SELECT * FROM Servico WHERE matricula = :numero")
    fun getServicos(numero: Long): Observable<List<Servico>>

    @Query("SELECT * FROM Servico ")
    fun getTotalServicosLeitura(): Observable<List<Servico>>

    @Query("SELECT * FROM Servico WHERE codigoTributo = 3 AND matricula = :numLigacao ")
    fun isParcelaIncorporada(numLigacao: Long): Observable<List<Servico>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(servico: List<Servico>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(servico: ServicoLeitura)

    @Delete
    fun delete(servico: ServicoLeitura)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertServico(servico: Servico)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateServico(servico: Servico)
}