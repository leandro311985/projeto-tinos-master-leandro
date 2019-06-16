package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Roteiro
import io.reactivex.Observable

@Dao
interface RoteiroDao {

    @Query("SELECT * FROM roteiro WHERE nome = :nome")
    fun getRoteiro(nome: String): Observable<Roteiro>

    @Query("SELECT * FROM roteiro")
    fun getRoteiros(): Observable<List<Roteiro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(roteiros: List<Roteiro>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roteiro: Roteiro)

    @Update
    fun updateRoteiro(roteiro: Roteiro)

}