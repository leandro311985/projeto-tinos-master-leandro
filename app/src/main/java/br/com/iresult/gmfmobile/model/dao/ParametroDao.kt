package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Parametro
import io.reactivex.Observable

@Dao
interface ParametroDao {

    @Query("SELECT distinct * FROM Parametro ")
    fun getParametro(): Observable<Parametro>

    @Query("SELECT * FROM parametro")
    fun getParametros(): Observable<List<Parametro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(parametros: List<Parametro>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parametro: Parametro)

    @Update
    fun updateParametro(parametro: Parametro)

}