package br.com.iresult.gmfmobile.utils

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import br.com.iresult.gmfmobile.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MapUtils(private val context: Context, private val mMap: GoogleMap) {

    private var distance = 0
    private var duration = 0

    companion object {
        private val DOT = Dot()
        private val GAP = Gap(16f)
        private val PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT)
    }

    fun fetchRoutesAndGetDuration(startPos: LatLng, endPos: LatLng, callback: (polyline: Polyline, duration: Int) -> Unit) {
        val url = getDirectionsUrl(startPos, endPos)
        doAsync {
            val rotas = downloadUrl(url)
            context.runOnUiThread {
                parseRoutes(rotas, callback)
            }
        }
    }

    private fun parseRoutes(json: String, callback: (polyline: Polyline, duration: Int) -> Unit) {

        val jObject = JSONObject(json)
        val routes: List<List<HashMap<*, *>>>? = parse(jObject)
        val points = ArrayList<LatLng>()
        val routeOptions = PolylineOptions()
        val builder = LatLngBounds.Builder()

        if (routes != null && routes.isNotEmpty()) {

            val path = routes[0]

            for (j in path.indices) {
                val point = path[j]

                if (point["lat"] != null && point["lng"] != null) {
                    val lat = java.lang.Double.parseDouble(point["lat"] as String)
                    val lng = java.lang.Double.parseDouble(point["lng"] as String)
                    val position = LatLng(lat, lng)
                    builder.include(position)
                    points.add(position)
                }
            }

            routeOptions.addAll(points)
            routeOptions.width(12f)
            routeOptions.color(ContextCompat.getColor(context, R.color.colorAccent))
            routeOptions.geodesic(true)

            val polyline = mMap.addPolyline(routeOptions)
            polyline?.pattern = PATTERN_POLYLINE_DOTTED

            callback.invoke(polyline!!, duration)
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val strOrigin = "origin=" + origin.latitude + "," + origin.longitude
        val strDest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        val mode = "mode=walking"
        val key = "key=" + context.getString(R.string.google_maps_key)
        val parameters = "$strOrigin&$strDest&$sensor&$mode&$key"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream!!))
            val sb = StringBuilder()

            var line: String?
            do {
                line = br.readLine()
                sb.append(line)
            } while (line != null)

            data = sb.toString()
            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream?.close()
            urlConnection?.disconnect()
        }
        return data
    }

    private fun parse(jObject: JSONObject): List<List<HashMap<*, *>>> {
        val routes = ArrayList<List<HashMap<*, *>>>()
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jSteps: JSONArray
        distance = 0
        duration = 0
        try {
            jRoutes = jObject.getJSONArray("routes")

            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                val path = ArrayList<HashMap<*, *>>()

                for (j in 0 until jLegs.length()) {

                    val jLeg = jLegs.get(j) as JSONObject
                    val jLegDistance = jLeg.get("distance") as JSONObject
                    val jLegDuration = jLeg.get("duration") as JSONObject

                    distance += jLegDistance.getInt("value")
                    duration += jLegDuration.getInt("value")

                    jSteps = jLeg.getJSONArray("steps")

                    for (k in 0 until jSteps.length()) {
                        val polyline: String = ((jSteps.get(k) as JSONObject).get(
                                "polyline") as JSONObject).get("points") as String
                        val list = decodePoly(polyline)

                        for (l in list.indices) {
                            val hm = HashMap<String, String>()
                            hm["lat"] = java.lang.Double.toString(list[l].latitude)
                            hm["lng"] = java.lang.Double.toString(list[l].longitude)
                            path.add(hm)
                        }
                    }
                    routes.add(path)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return routes
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }
}