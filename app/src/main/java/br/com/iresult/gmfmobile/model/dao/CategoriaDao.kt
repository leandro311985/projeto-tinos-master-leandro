package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.Categoria
import br.com.iresult.gmfmobile.model.bean.ParametroImpressao
import java.util.*
import io.reactivex.Observable


@Dao
interface CategoriaDao {

    @Query("SELECT * FROM Categoria where codCategoria = :codCategoria")
    fun getCategoria(codCategoria: String ):   Observable<List<Categoria>>

    @Query("SELECT * FROM Categoria"  )
    fun getCategorias( ):   Observable<List<Categoria>>

    @Query("SELECT valorLimite FROM Categoria where codCategoria = :codCategoria")
    fun getValorLimite(codCategoria: String) : Double


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categorias: List<Categoria>)
}