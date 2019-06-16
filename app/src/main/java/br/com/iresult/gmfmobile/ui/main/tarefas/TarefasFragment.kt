package br.com.iresult.gmfmobile.ui.main.tarefas

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Roteiro
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.main.estatistica.EstatisticaActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.informative.InformativosActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.RoteiroActivity
import br.com.iresult.gmfmobile.utils.BaseAdapter
import kotlinx.android.synthetic.main.fragment_tarefas.*
import kotlinx.android.synthetic.main.roteiro_item.view.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TarefasFragment : BaseFragment() {

    val viewModel: TarefasViewModel by viewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_tarefas

    override fun getTitle(): Int = R.string.tarefas_title

    override fun setupView() {
        recyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = BaseAdapter<Roteiro>(R.layout.roteiro_item) { roteiro, itemView ->
                itemView.nomeArquivo.text = roteiro.nome
                itemView.ruas_total.text = "Endereços (${roteiro.totalRuas})"
                itemView.ruas_restantes.text = "Faltam (${roteiro.totalRuas})"
                itemView.casas_total.text = "Imóveis (${roteiro.totalCasas})"
                itemView.casas_restantes.text = "Faltam (${roteiro.totalCasas})"
                itemView.porcentagem.text = "0%"
                itemView.ivStatistics.setOnClickListener {
                    context?.startActivity<EstatisticaActivity>()
                }
                itemView.setOnClickListener {
                    if (viewModel.notShowInformatives()) {
                        context?.startActivity<RoteiroActivity>(RoteiroActivity.ARG_ROTEIRO to roteiro.nome)
                    } else {
                        context?.startActivity<InformativosActivity>(InformativosActivity.ARG_ROTEIRO to roteiro)
                    }
                }
            }.also { adapter -> viewModel.roteiros.observe(this, Observer { adapter.setItems(it) }) }
        }
    }
}