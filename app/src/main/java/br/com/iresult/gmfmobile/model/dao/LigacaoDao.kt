package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Ligacao
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface LigacaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLigacao(vararg ligacao: Ligacao)

    @Update
    fun updateUnidade(ligacao: Ligacao)

    @Delete
    fun deleteUnidade(ligacao: Ligacao)

    @Update
    fun updateLigacao(vararg ligacao: Ligacao): Maybe<Int>

    @Query("SELECT * FROM unidade")
    fun all(): Observable<List<Ligacao>>

    @Query("SELECT * FROM unidade WHERE numeroMedidor = :numeroMedidor")
    fun searchByMedidor(numeroMedidor: String): Observable<List<Ligacao>>

    @Query("SELECT * FROM unidade WHERE nomeCliente LIKE '%' || :nomeCliente || '%'")
    fun searchByNome(nomeCliente: String): Observable<List<Ligacao>>

    @Query("SELECT * FROM unidade WHERE enderecoLigacao LIKE '%' || :enderecoLigacao || '%' order by numReg")
    fun searchByEndereco(enderecoLigacao: String): Observable<List<Ligacao>>

    @Query("SELECT * FROM unidade WHERE numeroLigacao = :numeroLigacao")
    fun searchByMatricula(numeroLigacao: String): Observable<List<Ligacao>>

    @Query("SELECT statusRegistro FROM unidade" )
    fun searchAllStaReg(): Observable<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnidades(unidades: List<Ligacao>)
}