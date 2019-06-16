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
@Entity(tableName = "address")
data class Address(
        @PrimaryKey
        @SerializedName("NOME")
        val nome: String,
        @SerializedName("BAIRRO")
        val bairro: String?,
        @SerializedName("CEP")
        val cep: String?,
        @SerializedName("UNIDADES")
        val ligacoes: List<Ligacao>?,
        val latitude: Double?,
        val longitude: Double?
) : Parcelable {

        fun formattedAddress() = "$bairro $cep"

        fun completeAddress() = "$nome, $bairro, $cep"
}