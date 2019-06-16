package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.Dominio
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import java.util.*
import io.reactivex.Observable


@Dao
interface DominioDao {

    @Query("SELECT * FROM Dominio where nomeDominio = :nomeDominio and valorDominio = :valorDominio")
    fun getDominio(nomeDominio: String , valorDominio: String):   Observable<Dominio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categorias: List<Dominio>)
}