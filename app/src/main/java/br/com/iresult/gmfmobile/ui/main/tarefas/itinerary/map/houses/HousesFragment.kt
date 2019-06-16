package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.houses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.RoteiroViewModel
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.ui.widget.HomeStatusView
import br.com.iresult.gmfmobile.utils.BaseAdapter
import kotlinx.android.synthetic.main.fragment_houses_map.*
import kotlinx.android.synthetic.main.rua_item.view.*
import kotlinx.android.synthetic.main.servicos_leituras_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created  by dafle.cardoso on 21, May, 2019
 */
class HousesFragment: Fragment() {

    val viewModel : RoteiroViewModel by viewModel()

    companion object {

        const val EXTRA_ADDRESS = "EXTRA_ADDRESS"

        fun newInstance(args: Bundle?): HousesFragment {
            return HousesFragment().apply {
                this.arguments = args
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_houses_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Address?>(EXTRA_ADDRESS)?.let {
            tvStreetName.text = it.nome
            tvCepName.text = "${it.bairro} ${it.cep}"
            viewModel.fetchHouses(it.nome)
        }

        ivBack.setOnClickListener {
            parentFragment?.childFragmentManager?.popBackStack()
        }
        recyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        recyclerView.adapter = BaseAdapter(R.layout.item_home) { home: Ligacao, homeStatusView ->

            (homeStatusView as HomeStatusView).let { homeView ->
                homeView.tvNumberHome.text = home.numero
                homeView.setupLayout(home.status?.let { status -> HomeStatus.valueOf(status) } ?: run { HomeStatus.NONE })
            }
        }.also { adapter ->
            viewModel.ligacao.observe(this, Observer {
                adapter.setItems(viewModel.formattedHouses(it, pedding = false, finished = false, reversed = false))
            })
        }
    }
}