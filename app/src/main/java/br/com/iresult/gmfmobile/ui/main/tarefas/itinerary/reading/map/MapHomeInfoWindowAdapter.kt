package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import br.com.iresult.gmfmobile.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.marker_info_house.view.*
import me.crosswall.lib.coverflow.core.Utils

/**
 * Created  by dafle.cardoso on 21, May, 2019
 */
class MapHomeInfoWindowAdapter(private val context: Context, private val bigFont: Boolean) : GoogleMap.InfoWindowAdapter {

    private val mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_house, null)

    private fun randomWindowText(marker: Marker?, view: View) {
        val title = marker?.title
        view.tvStreet.text = title
        marker?.snippet?.split("||")?.let {
            view.tvCity.text = it.firstOrNull()
            if (it.size > 1) {
                view.tvTime.text = it.lastOrNull()
            }
        }

        if (bigFont){
            view.tvTime.textSize = Utils.convertDpToPixel(12f, context)
        }
    }

    override fun getInfoContents(marker: Marker?): View {
        randomWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker?): View {
        randomWindowText(marker, mWindow)
        return mWindow
    }
}