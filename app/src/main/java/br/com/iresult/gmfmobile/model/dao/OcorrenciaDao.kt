package br.com.iresult.gmfmobile.model.dao

import androidx.room.*
import br.com.iresult.gmfmobile.model.bean.Ocorrencia

@Dao
interface OcorrenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertOcorrencia(vararg ocorrencia: Ocorrencia)

    @Update
    fun updateOcorrencia(ocorrencia: Ocorrencia)

    @Delete
    fun deleteOcorrencia(ocorrencia: Ocorrencia)


    @Query("SELECT * FROM Ocorrencia WHERE codigo = :codigo")
    fun buscarOcorrencia(codigo: Int): Ocorrencia



}