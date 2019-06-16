package br.com.iresult.gmfmobile.model.bean

import com.google.gson.annotations.SerializedName

data class GeocodedAddress(
        @SerializedName("results") val results: List<GeocodedAddressResult?>?,
        @SerializedName("status") val status: String?
)

data class GeocodedAddressResult(
        @SerializedName("address_components") val addressComponents: List<GeocodedAddressAddressComponent?>?,
        @SerializedName("formatted_address") val formattedAddress: String?,
        @SerializedName("geometry") val geometry: GeocodedAddressGeometry?,
        @SerializedName("place_id") val placeId: String?,
        @SerializedName("types") val types: List<String?>?
)

data class GeocodedAddressGeometry(
        @SerializedName("location") val location: GeocodedAddressLocation?,
        @SerializedName("location_type") val locationType: String?,
        @SerializedName("viewport") val viewport: GeocodedAddressViewport?
)

data class GeocodedAddressLocation(
        @SerializedName("lat") val lat: Double?,
        @SerializedName("lng") val lng: Double?
)

data class GeocodedAddressViewport(
        @SerializedName("northeast") val northeast: GeocodedAddressNortheast?,
        @SerializedName("southwest") val southwest: GeocodedAddressSouthwest?
)

data class GeocodedAddressSouthwest(
        @SerializedName("lat") val lat: Double?,
        @SerializedName("lng") val lng: Double?
)

data class GeocodedAddressAddressComponent(
        @SerializedName("long_name") val longName: String?,
        @SerializedName("short_name") val shortName: String?,
        @SerializedName("types") val types: List<String?>?
)

data class GeocodedAddressNortheast(
        @SerializedName("lat") val lat: Double?,
        @SerializedName("lng") val lng: Double?
)