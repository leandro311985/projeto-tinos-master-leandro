package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.adresses.AdressesBottomSheedFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created  by dafle.cardoso on 21, May, 2019
 */

class MapsHomeContainerBottomSheetDialogFragment: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
                .replace(R.id.content_frame, AdressesBottomSheedFragment.newInstance(activity?.intent?.extras))
                .commit()
    }
}