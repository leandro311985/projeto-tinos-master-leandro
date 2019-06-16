package br.com.iresult.gmfmobile.ui.base

import android.content.Context
import android.os.Bundle
import br.com.iresult.gmfmobile.R
import kotlinx.android.synthetic.main.print_dialog.*

class PrintDialog(context: Context) : GmfDialog(context) {

    private var printListener: (() -> Unit)? = null

    override fun getLayoutResource(): Int = R.layout.print_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_cancel.setOnClickListener { dismiss() }
        btn_print.setOnClickListener { printListener?.invoke() }
    }

    fun setPrintListener(dismissListener: () -> Unit): PrintDialog {
        this.printListener = dismissListener
        return this
    }
}