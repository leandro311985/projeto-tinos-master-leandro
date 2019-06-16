package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.iresult.gmfmobile.model.bean.Justificativa

@Dao
interface JustificativaDao {

    @Query("SELECT * FROM Justificativa") fun getJustificativa(): List<Justificativa>

}