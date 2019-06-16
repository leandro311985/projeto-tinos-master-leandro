package br.com.iresult.gmfmobile.service

import br.com.iresult.gmfmobile.model.bean.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @FormUrlEncoded
    @POST("api.php")
    fun login(@Field("usuario") usuario: String, @Field("senha") senha: String) : Observable<LoginResponse>
}