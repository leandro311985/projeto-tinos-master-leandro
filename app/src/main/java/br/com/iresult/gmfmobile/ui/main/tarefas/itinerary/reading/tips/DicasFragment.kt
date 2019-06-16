package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import kotlinx.android.synthetic.main.fragment_dicas.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DicasFragment : Fragment() {

    val viewModel : LeituraViewModel by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_dicas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
        btnVoltar.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

        viewModel.unidade.observe(viewLifecycleOwner, Observer { unidade ->
            tvTip.text = unidade.dica

            if (unidade.dica == null || unidade.dica?.trim() == "") {
                tvTip.text = "Nenhuma dica a ser mostrada"
            }
        })
    }
}