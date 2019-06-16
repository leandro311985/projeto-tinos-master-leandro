package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.PreenchimentoEstatistica
import io.reactivex.Observable

@Dao
interface PreenchimentoEstatisticaDao {

    @Query("SELECT * FROM PreenchimentoEstatistica WHERE roteiroNomeReg = :nome")
    fun getEstatistica(nome: String): Observable<PreenchimentoEstatistica>

    @Query("SELECT * FROM PreenchimentoEstatistica WHERE ruaNome = :nome")
    fun getEstatisticaByRua(nome: String): Observable<PreenchimentoEstatistica>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstatistica(vararg data: PreenchimentoEstatistica)

}