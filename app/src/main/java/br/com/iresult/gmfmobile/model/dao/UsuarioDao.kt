package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.Usuario
import io.reactivex.Maybe

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario LIMIT 1")
    fun getUsuario(): Maybe<List<Usuario>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg usuario: Usuario)
}