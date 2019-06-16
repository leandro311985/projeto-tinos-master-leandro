package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.model.bean.Rubrica
import io.reactivex.Observable


@Dao
interface RubricaDao {

    @Query("SELECT * FROM Rubrica where codigoRubrica = :codigoRubrica")
    fun getRubrica(codigoRubrica: Int): Observable<List<Rubrica>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rubrica: List<Rubrica>)


}