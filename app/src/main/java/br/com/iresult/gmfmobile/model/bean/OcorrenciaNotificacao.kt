package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class OcorrenciaNotificacao(
        @PrimaryKey @SerializedName("NUM_REG") val numReg: String,
        @SerializedName("COD_OCOR") val codOcorrencia: String,
        @SerializedName("DESC_OCOR") val descricaoOcorrencia: String,
        @SerializedName("STA_EXE") val  statusExecucao: String
) : Parcelable