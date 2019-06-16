package br.com.iresult.gmfmobile.repository

import br.com.iresult.gmfmobile.model.bean.GeocodedAddress
import br.com.iresult.gmfmobile.service.GeocodingService
import io.reactivex.Single

class GeocodingRepository(private val geocodingService: GeocodingService) {

    fun geocode(address: String, key: String): Single<GeocodedAddress> {
        val formattedAddress = address
                .replace("R ", "RUA ")
                .replace("AV ", "AVENIDA ")
                .replace("  ", "+")
                .replace(" ", "+")
        val type = "street_address"
        return geocodingService.geocode(formattedAddress, type, key)
    }
}