package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by victorfernandes on 18/02/19.
 */
@Parcelize
@Entity(tableName = "tarefa")
data class Tarefa(
        @PrimaryKey(autoGenerate = true) val uuid: Long,
        @SerializedName("RUAS") val addresses: List<Address>?
) : Parcelable