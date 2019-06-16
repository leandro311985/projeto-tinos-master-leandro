package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.model.bean.Address
import kotlinx.android.synthetic.main.activity_leitura.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by victorfernandes on 17/02/19.
 */
class LeituraActivity : AppCompatActivity() {

    val viewModel: LeituraViewModel by viewModel()

    companion object {
        const val ARG_ROTEIRO = "ARG_ROTEIRO"
        const val ARG_UNIDADE = "unidade"
        const val ARG_RUA = "address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_leitura)

        intent.extras?.apply {
            val address: Address = getParcelable(ARG_RUA) ?: return
            val unidade: Ligacao = getParcelable(ARG_UNIDADE) ?: return
            val routing: String = getString(ARG_ROTEIRO) ?: return
            viewModel.initModel(address, unidade, routing)
        }
        setupBindings()
    }

    fun setupBindings() {

        viewModel.unidade.observe(this, Observer { unidade ->
            morador.text = unidade.nomeCliente
            status.text = viewModel.statusRegistroDescription(unidade.statusRegistro)
            numero.text = unidade.numero
            codigo.text = unidade.numReg.toLong().toString()
            endereco.text = viewModel.getFormattedAddress(unidade?.descricaoComplemento)
            hidrometro.text = unidade.numeroMedidor
            matricula.text = String.format("%09d", unidade?.numeroLigacao)
        })
    }
}