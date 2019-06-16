package br.com.iresult.gmfmobile.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import io.reactivex.Single

class AsyncGeocoder(context: Context) {

    private val geocoder = Geocoder(context)

    @JvmOverloads
    fun geocode(locationName: String,
                lowerLeftLatitude: Double = 0.0, lowerLeftLongitude: Double = 0.0,
                upperRightLatitude: Double = 0.0, upperRightLongitude: Double = 0.0): Single<Address?> {
        return Single.fromCallable {
            geocoder.getFromLocationName(locationName, 1,
                    lowerLeftLatitude, lowerLeftLongitude,
                    upperRightLatitude, upperRightLongitude).firstOrNull()
        }
                .doAfterTerminate {
                    Log.e("asda", "asdasd")
                }
    }
}