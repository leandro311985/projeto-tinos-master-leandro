package br.com.iresult.gmfmobile.model.bean

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by victorfernandes on 18/02/19.
 */

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponse(
        @SerializedName("usuario")
        val usuario: Usuario,
        @SerializedName("arquivos")
        val roteiros: List<Roteiro>?
) : Parcelable