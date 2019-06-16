package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.finish

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import kotlinx.android.synthetic.main.activity_transmissao.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishTaskActivity : AppCompatActivity() {

    val viewModel: FinishTaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transmissao)
        btn_send.setOnClickListener { getActionDialog(this).show() }
        btn_back.setOnClickListener { finish() }
    }

    private fun getActionDialog(context: Context): SimpleDialog {
        val dialog = SimpleDialog(context, getString(R.string.transmissao_dialog_action_title))
        dialog.setupActionButton(getString(R.string.yes), getString(R.string.cancel)) {
            //TODO Finish task, come back activity
            dialog.dismiss()
        }
        dialog.setDismissListener {
            // TODO dismiss listener (not called on ok button)
        }
        return dialog
    }

    private fun getSimpleDialog(context: Context): SimpleDialog {
        val dialog = SimpleDialog(context, getString(R.string.transmissao_dialog_simple_title))
        dialog.setupCloseButton(getString(R.string.ok))
        return dialog
    }

    private fun getIconDialog(context: Context): SimpleDialog {
        val dialog = SimpleDialog(context, getString(R.string.transmissao_dialog_simple_title))
        dialog.showIcon(true)
        dialog.setupCloseButton(getString(R.string.ok))
        return dialog
    }
}