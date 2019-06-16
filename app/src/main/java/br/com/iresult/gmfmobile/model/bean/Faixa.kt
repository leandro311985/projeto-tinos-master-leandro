package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.iresult.gmfmobile.utils.NumberUtils
import br.com.iresult.gmfmobile.utils.Utils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "faixa")
@Parcelize
data class Faixa(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_CATEG") var codigoCategoria: String?,
        @SerializedName("COD_FAT") var codigoFaturamento: Int,
        @SerializedName("TPO_LIG") var tipoLigacao: Int,
        @SerializedName("CON_MIN") var consumoMinimo: Double = 0.0,
        @SerializedName("CON_MAX") var consumoMaximo: Double = 0.0,
        @SerializedName("VAL_TARI") var tarifaAgua: Double = 0.0,
        @SerializedName("VAL_TARI_E") var tarifaEsgoto: Double = 0.0,
        @SerializedName("VAL_DED") var deduzirAgua: Double = 0.0,
        @SerializedName("SIN_DED") var sinalDeducao : String,
        @SerializedName("VAL_DED_E") var deduzirEsgoto: Double = 0.0,
        @SerializedName("SIN_DED_E") var sinalEsgoto: String?,
        @SerializedName("VAL_TARI_T") var tarifaTratamento: Double = 0.0,
        @SerializedName("VAL_DED_T") var deduzirTratamento: Double = 0.0,
        @SerializedName("SIN_DED_T") var sinalTratamento: String?
) : Parcelable {
    fun getDesc() : String{

            if(consumoMinimo == 0.0)
                return "ate " + Utils.Companion.setField(NumberUtils.Companion.toString( consumoMaximo, 0), 3, 0);

            if(consumoMaximo > 99999)
                return "acima " + Utils.Companion.setField(NumberUtils.Companion.toString(consumoMinimo, 0), 3, 0);

            return "de " + Utils.Companion.setField(NumberUtils.Companion.toString(consumoMinimo,0 ), 3, 0) + " a " + Utils.Companion.setField(NumberUtils.Companion.toString(consumoMaximo,0), 3, 0);

    }

    fun getTotal(): Double {
        return tarifaAgua + tarifaEsgoto + tarifaTratamento
    }
}