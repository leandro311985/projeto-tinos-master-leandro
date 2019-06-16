package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.OcorrenciaLeitura

@Dao
interface OcorrenciaLeituraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertOcorrencia(vararg ocorrenciaLeitura: OcorrenciaLeitura)

    @Query("SELECT * FROM OcorrenciaLeitura WHERE numeroLigacao = :numeroLigacao") fun getOcorrenciaLeitura(numeroLigacao: Long): OcorrenciaLeitura

}