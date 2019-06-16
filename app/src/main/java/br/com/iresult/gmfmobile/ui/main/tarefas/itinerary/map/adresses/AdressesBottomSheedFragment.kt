package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.adresses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.RoteiroViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.MapsHomeActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.houses.HousesFragment
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.houses.HousesFragment.Companion.EXTRA_ADDRESS
import br.com.iresult.gmfmobile.utils.BaseAdapter
import kotlinx.android.synthetic.main.fragment_adresses_bottom_sheet.*
import kotlinx.android.synthetic.main.rua_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdressesBottomSheedFragment: Fragment() {

    val viewModel : RoteiroViewModel by viewModel()

    companion object {

        fun newInstance(bundle: Bundle?): AdressesBottomSheedFragment {
            val fragment = AdressesBottomSheedFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adresses_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fileName = arguments?.getString(MapsHomeActivity.EXTRA_BUNDLE_TASK_NAME) ?: return
        viewModel.fetchAdressesByFileName(fileName)


//        recyclerView.let {
//            it.layoutManager = LinearLayoutManager(context)
//            it.adapter = BaseAdapter(R.layout.rua_item) { address: Address, itemView ->
//                itemView.nomeRua.text = address.nome
//                itemView.cepRua.text = address.formattedAddress()
//                itemView.setOnClickListener {
//
//                    this.parentFragment?.childFragmentManager?.beginTransaction()
//                            ?.replace(R.id.content_frame, HousesFragment.newInstance(Bundle().apply { putParcelable(EXTRA_ADDRESS, address) }))
//                            ?.addToBackStack(null)
//                            ?.commit()
//                }
//            }.also { adapter ->
//                viewModel.address.observe(this, Observer {
//                    adapter.setItems(it)
//                })
//            }
//        }

//        recyclerView.let {
//            it.layoutManager = LinearLayoutManager(context)
//            it.adapter = BaseAdapter(R.layout.rua_item) { address: Address, itemView ->
//                itemView.nomeRua.text = address.nome
//                itemView.cepRua.text = address.formattedAddress()
//                itemView.setOnClickListener {
//
//                }
//            }.also { adapter ->
//                viewModel.address.observe(this, Observer {
//                    adapter.setItems(it)
//                })
//            }
//        }
    }
}