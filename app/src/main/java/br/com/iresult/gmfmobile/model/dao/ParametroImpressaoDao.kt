package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import io.reactivex.Observable

@Dao
interface ParametroImpressaoDao {

    @Query("SELECT * FROM ParametroImpressao limit 1")
    fun getParametroImpressao(): Observable<ParametroImpressao>

    @Query("SELECT * FROM parametroImpressao")
    fun getParametroImpressaos(): Observable<List<ParametroImpressao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(parametroImpressaos: List<ParametroImpressao>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parametroImpressao: ParametroImpressao)

    @Update
    fun updateParametroImpressao(parametroImpressao: ParametroImpressao)

}