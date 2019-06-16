package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import kotlinx.android.synthetic.main.fragment_dados_cadastrais.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DataRegistersFragment : Fragment() {

    val viewModel : LeituraViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dados_cadastrais, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btBack.setOnClickListener { goBack() }
        btCancel.setOnClickListener { goBack() }
        btSave.setOnClickListener {
        }

        viewModel.unidade.observe(this, Observer {
            economiaCom.text = it.economiaComercial.toString()
            economiaInd.text = it.economiaIndustrial.toString()
            economiaPub.text = it.economiaPublica.toString()
            economiaRes.text = it.economiaResidencial.toString()
            hidrometro.text = it.numeroMedidor
        })
    }

    private fun goBack() {
        NavHostFragment.findNavController(this).popBackStack()
    }
}