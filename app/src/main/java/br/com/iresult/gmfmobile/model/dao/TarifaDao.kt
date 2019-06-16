package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.*
import io.reactivex.Observable


@Dao
interface TarifaDao {

    @Query("SELECT * FROM Tarifa where codigoCategoria = :codigoCategoria and codigoFaturamento = :codigoFaturamento")
    fun buscaValorMinimo(codigoCategoria: String, codigoFaturamento: Int ): Observable<List<Tarifa>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tarifas: List<Tarifa>)

}