package br.com.iresult.gmfmobile.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import br.com.iresult.gmfmobile.R
import kotlinx.android.synthetic.main.simple_dialog.*

class SimpleDialog(context: Context, private val dialogTitle: String) : GmfDialog(context) {

    private var showIcon: Boolean = false
    private var singleButton: Boolean = true
    private var okListener: (() -> Unit)? = null
    private var dismissListener: (() -> Unit)? = null
    private var actionTitle: String? = null
    private var cancelTitle: String? = null
    private var closeTitle: String? = null

    override fun getLayoutResource(): Int = R.layout.simple_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title.text = dialogTitle
        icon.visibility = if (showIcon) View.VISIBLE else View.GONE

        setCancelable(false)

        if (singleButton) {
            btn_cancel.visibility = View.GONE
            btn_ok.visibility = View.GONE
            btn_close.visibility = View.VISIBLE

            btn_close.setOnClickListener {
                dismiss()
                dismissListener?.invoke()
            }
        } else {
            btn_cancel.visibility = View.VISIBLE
            btn_ok.visibility = View.VISIBLE
            btn_close.visibility = View.GONE

            btn_cancel.setOnClickListener {
                dismiss()
                dismissListener?.invoke()
            }
            btn_ok.setOnClickListener { okListener?.invoke() }
        }

        actionTitle?.let { btn_ok.text = it }
        cancelTitle?.let { btn_cancel.text = it }
        closeTitle?.let { btn_close.text = it }
    }

    fun showIcon(showIcon: Boolean): SimpleDialog {
        this.showIcon = showIcon
        return this
    }

    fun setupCloseButton(closeTitle: String): SimpleDialog {
        singleButton = true
        this.closeTitle = closeTitle
        return this
    }

    fun setupActionButton(actionTitle: String, cancelTitle: String, okListener: (() -> Unit)? = null): SimpleDialog {
        singleButton = false
        this.actionTitle = actionTitle
        this.okListener = okListener
        this.cancelTitle = cancelTitle
        return this
    }

    fun setDismissListener(dismissListener: () -> Unit): SimpleDialog {
        this.dismissListener = dismissListener
        return this
    }

}