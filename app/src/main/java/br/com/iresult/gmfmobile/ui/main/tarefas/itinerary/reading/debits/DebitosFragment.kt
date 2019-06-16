package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.debits

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import kotlinx.android.synthetic.main.fragment_debitos_leitura.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DebitosFragment : androidx.fragment.app.Fragment() {

    val viewModel : LeituraViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_debitos_leitura, null)

        view.btBack.setOnClickListener {

            NavHostFragment.findNavController(this@DebitosFragment).popBackStack()
        }

        view.btBack.setOnClickListener {

            NavHostFragment.findNavController(this@DebitosFragment).popBackStack()
        }

        view.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBindings()
    }

    fun setupBindings() {

        viewModel.unidade.observe(viewLifecycleOwner, Observer { unidade ->

//            view?.recycler?.adapter = unidade?.debitos?.let { DebitoAdapter(it){
//
//            } }

        })

    }

}