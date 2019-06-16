package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "notificacao")
@Parcelize
data class Notificacao(@PrimaryKey @SerializedName("NUM_REG") val numReg: Int?,
                       @SerializedName("NUM_LIG") val descricao: Int?,
                       @SerializedName("REF") val ref: Int?,
                       @SerializedName("VAL_FAT") val valFat: Int?
): Parcelable