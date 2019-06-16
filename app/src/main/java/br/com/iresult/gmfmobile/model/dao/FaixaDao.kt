package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Faixa
import br.com.iresult.gmfmobile.model.bean.Leitura
import io.reactivex.Observable


@Dao
interface FaixaDao {

    @Query("SELECT consumoMaximo FROM Faixa WHERE codigoCategoria = :codigoCategoria")
    fun getLimiteTarifa(codigoCategoria: String): Int


    @Query("SELECT * FROM Faixa WHERE codigoCategoria like :codigoCategoria and codigoFaturamento = :codigoFaturamento and tipoLigacao = :tipoLigacao")
    fun cascata(codigoCategoria: String, codigoFaturamento : Int, tipoLigacao: Int ) : Observable<List<Faixa>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(faixas: List<Faixa>)


}