package br.com.iresult.gmfmobile.service

import br.com.iresult.gmfmobile.model.bean.GeocodedAddress
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {

    @GET("geocode/json")
    fun geocode(@Query("address", encoded = true) address: String,
                @Query("result_type") resultType: String,
                @Query("key") key: String): Single<GeocodedAddress>
}