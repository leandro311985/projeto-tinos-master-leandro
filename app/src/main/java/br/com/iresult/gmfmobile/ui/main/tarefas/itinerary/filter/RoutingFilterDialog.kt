package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.filter

import android.content.Context
import android.os.Bundle
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.GmfDialog
import kotlinx.android.synthetic.main.dialog_filter_routing.*

class RoutingFilterDialog(context: Context,
                          private val pending: Boolean,
                          private val finished: Boolean,
                          private val reversed: Boolean): GmfDialog(context) {

    override fun getLayoutResource() = R.layout.dialog_filter_routing
    var onConfirm: ((pending: Boolean, finished: Boolean, reversed: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ckPending.isChecked = pending
        ckFinished.isChecked = finished
        ckReversed.isChecked = reversed

        btCancel.setOnClickListener { dismiss() }
        btConfirm.setOnClickListener {
            super.dismiss()
            onConfirm?.invoke(ckPending.isChecked, ckFinished.isChecked, ckReversed.isChecked)
        }
    }
}