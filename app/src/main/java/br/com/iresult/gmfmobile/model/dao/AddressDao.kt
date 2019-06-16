package br.com.iresult.gmfmobile.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import br.com.iresult.gmfmobile.model.bean.Address
import io.reactivex.Maybe

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pets: List<Address>)

    @Update
    fun update(address: Address): Maybe<Int>
}