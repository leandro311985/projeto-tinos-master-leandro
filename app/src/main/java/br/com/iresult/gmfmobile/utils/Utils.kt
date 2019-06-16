package br.com.iresult.gmfmobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        fun convertDpToPixel(dp: Float, context: Context): Float {
            return dp * (context.getResources().getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px A value in px (pixels) unit. Which we need to convert into db
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent dp equivalent to px value
         */
        fun convertPixelsToDp(px: Float, context: Context): Float {
            return px / (context.getResources().getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun isLocationEnabled(context: Context): Boolean {
            var locationMode = 0
            val locationProviders: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.contentResolver, Settings
                            .Secure.LOCATION_MODE)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF

            } else {
                locationProviders = Settings.Secure.getString(context.contentResolver, Settings
                        .Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }

        fun bitmapDescriptorFromVector(context: Context,
                                       @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {

            ContextCompat.getDrawable(context, vectorDrawableResourceId)?.let { vectorDrawable ->
                vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth,
                        vectorDrawable.intrinsicHeight)
                val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                vectorDrawable.draw(canvas)
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }
            return null
        }

        /**
         * Realiza o cálculo do dígito verificador
         *
         * @param sBloco
         * @return
         */
        fun  calcularDV(sBloco: String): Int {

            var iX = 0
            var iY = 0
            var iProd = 0
            var iNumpos = 0
            val nDigit: Int
            var sNum: String

            var i = sBloco.length
            while (i >= 1) {

                for (j in 2 downTo 1) {

                    if (i <= sBloco.length && i > 0) {
                        try {
                            iNumpos =  Integer.parseInt(sBloco.substring(i - 1, i))
                        } catch (e:  Exception) {
                        }

                        i--
                        iProd = iNumpos * j

                        if (iProd > 9) {
                            sNum =  iProd.toString()
                            try {
                                iX = (iX + Integer.parseInt(sNum.substring(0, 1))
                                        + Integer.parseInt(sNum.substring(1, 2)))
                            } catch (e: Exception) {
                            }

                        } else {
                            iX = iX + iProd
                        }
                    } else {
                        break
                    }

                }

                i++
                i--
            }

            iY = iX % 10

            if (iY == 0)
                nDigit = 0
            else
                nDigit = 10 - iY

            return nDigit

        }

        fun getNextSpaceIndex(text: String?, limit: Int): Int {

            var index = 0

            if (text != null && text.length > limit) {

                index = text.indexOf(" ", limit + 1)
                index = if (index < 0) limit else index + 1
            }

            return index

        }
        fun splitInParts(s: String, partLength: Int): Array<String?> {
            val len = s.length

            // Number of parts
            val nparts = (len + partLength - 1) / partLength
            val parts = arrayOfNulls<String>(nparts)

            // Break into parts
            var offset = 0
            var i = 0
            while (i < nparts) {
                parts[i] = s.substring(offset, Math.min(offset + partLength, len))
                offset += partLength
                i++
            }

            return parts
        }

        /**
         * Retorna uma String com n espaços
         */
        fun space(qtd: Int): String {

            var s = ""

            for (i in 1..qtd) {
                s = "$s "
            }

            return s
        }

        fun setField(field: String?, tamanho: Int, direcao: Int): String {

            var s = field ?: ""

            if (direcao == 1)
            // espaços à direita
                s = s + space(tamanho - s.length)
            else if (direcao == 0)
            // espaços à esquerda
                s = space(tamanho - s.length) + s

            return s

        }


        fun setField(field : Int ,  tamanho: Int): String {


            return space(tamanho - field.toString().length) + field.toString();

        }

        fun getDataDDMMYYYY(data : Date) : String{
            val sdf :SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return sdf.format(data)
        }
    }
}