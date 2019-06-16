package br.com.iresult.gmfmobile.ui.main.tarefas.informative

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.RoteiroActivity
import br.com.iresult.gmfmobile.utils.BaseAdapter
import kotlinx.android.synthetic.main.activity_informativos.*
import kotlinx.android.synthetic.main.item_informative.view.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by victorfernandes on 23/02/19.
 * Updated by daflecardoso in 07/05/19
 */
class InformativosActivity : AppCompatActivity() {

    companion object {
        const val ARG_ROTEIRO = "ARG_ROTEIRO"
    }

    val viewModel: InformativosViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informativos)

        intent.extras?.getParcelable<Roteiro>(ARG_ROTEIRO)?.let { roteiro ->

            recyclerView.let {
                it.layoutManager = LinearLayoutManager(this)
                it.adapter = BaseAdapter<String?>(R.layout.item_informative) { message, itemView ->
                    itemView.tvTip.text = message
                }.also { adapter ->
                    val parametros = roteiro.parametros?.first()
                    adapter.setItems(listOf("${parametros.mensagem1}\n${parametros.mensagem2}\n${parametros.mensagem3}"))
                }
            }

            btNext.setOnClickListener {
                finish()
                viewModel.verifyNotShowAgain(checkBoxNotShowAgain.isChecked)
                startActivity<RoteiroActivity>(RoteiroActivity.ARG_ROTEIRO to roteiro.nome)
            }
        }
    }
}