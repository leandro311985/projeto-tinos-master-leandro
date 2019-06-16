package br.com.iresult.gmfmobile.ui.main.impressao

import android.content.Context
import android.os.Bundle
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.GmfDialog
import kotlinx.android.synthetic.main.dialog_filter_routing.*
import kotlinx.android.synthetic.main.dialog_filter_routing.btCancel
import kotlinx.android.synthetic.main.dialog_filter_routing.btConfirm
import kotlinx.android.synthetic.main.dialog_impressao.*

class ImpressaoDialog(context: Context): GmfDialog(context) {

    override fun getLayoutResource() = R.layout.dialog_impressao
    var onConfirm: ((imprime50Itens: Boolean, imprimeBackup: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        radio50Itens.isChecked = true
        radioBackup.isChecked = false

        btCancel.setOnClickListener { dismiss() }
        btConfirm.setOnClickListener {
            super.dismiss()
            onConfirm?.invoke(radio50Itens.isChecked, radioBackup.isChecked)
        }
    }
}