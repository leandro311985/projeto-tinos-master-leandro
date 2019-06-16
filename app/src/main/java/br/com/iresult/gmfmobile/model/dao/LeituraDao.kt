package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Leitura
import io.reactivex.Observable

@Dao
interface LeituraDao {

    @Query("SELECT max(sqLeitura) FROM Leitura where statusRegistro  in ( 1  , 2 )  ")
    fun getMaxSeqLeitura(): Observable<Long>

    @Query("SELECT * FROM Leitura  where numeroLigacao = :numeroLigacao")
    fun getLeitura(numeroLigacao : Long): Observable<List<Leitura>>

    @Query("SELECT * FROM leitura order by dataLeitura, horaLeitura")
    fun getLeituras(): Observable<List<Leitura>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(leituras: List<Leitura>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(leitura: Leitura)

    @Update
    fun updateLeitura(leitura: Leitura)

    @Query("SELECT * FROM leitura where statusRegistro > 0  order by dataLeitura, horaLeitura")
    fun getLeiturasBackup(): Observable<List<Leitura>>

    @Query("SELECT * FROM Leitura  where statusRegistro > 0 order by dataLeitura desc, horaLeitura desc limit 50")
    fun getLeituras50(): Observable<List<Leitura>>

}