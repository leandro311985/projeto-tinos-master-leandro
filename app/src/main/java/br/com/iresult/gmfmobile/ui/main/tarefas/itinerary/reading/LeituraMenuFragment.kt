package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.Visita
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.ui.base.FormaEntregaDialog
import br.com.iresult.gmfmobile.ui.base.PrintDialog
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map.MapHomeActivity
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import kotlinx.android.synthetic.main.fragment_leitura_menu.*
import net.danlew.android.joda.JodaTimeAndroid
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LeituraMenuFragment : Fragment() {

    val viewModel: LeituraViewModel by sharedViewModel()
    var qtdTentativas: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        qtdTentativas  = 0
        JodaTimeAndroid.init(activity);
        return inflater.inflate(R.layout.fragment_leitura_menu, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCardClicks()

        leitura.addOnChangeListener {
            viewModel.setLeitura(it)
        }

        btClose.setOnClickListener {
            qtdTentativas++
            var visita: Visita = Visita()
            if (!viewModel.leitura.value?.isEmpty()!!) {
                visita = Visita(viewModel.leituraRepository.dataBase,viewModel.mUnidade.value,viewModel.leitura.value)
            }

            viewModel.printInvoice(qtdTentativas, visita)
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel.unidade.observe(viewLifecycleOwner, Observer { unidade ->
            total_debitos.text = "0"

        })

        viewModel.ocorrencia.observe(viewLifecycleOwner, Observer { oco ->
            tvBadge.visibility = View.VISIBLE
            NavHostFragment.findNavController(this).popBackStack()
        })

        viewModel.enableDisableJustify.observe(viewLifecycleOwner, Observer {
            leitura.isEnabled = !it
        })

//        viewModel.isHabilitarCampoTexto.observe(viewLifecycleOwner, Observer { habilitar ->
//            leitura.isEnabled = habilitar
//        })

        viewModel.leitura.observe(viewLifecycleOwner, Observer { leituraStr ->
            if (leitura.text.toString() != leituraStr) {
                leitura.setText(leituraStr)
            }
        })

        viewModel.deveHabilitarBotaoEnvio().observe(viewLifecycleOwner, Observer { habilitar ->
            btClose.isEnabled = habilitar
        })

        viewModel.habilitarBotaoOcorrencia().observe(viewLifecycleOwner, Observer {
            //view.ocorrencia.isEnabled = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            val dialog = SimpleDialog(requireContext(), error)
            dialog.setupCloseButton(getString(R.string.ok))
            dialog.show()

        })

        viewModel.erroLeitura.observe(viewLifecycleOwner, Observer {
            val dialog = SimpleDialog(requireContext(), it)
            dialog.setupActionButton(getString(R.string.yes), getString(R.string.cancel)) {
                dialog.dismiss()
            }
            dialog.setDismissListener {

            }
            dialog.show()

        })

        viewModel.updatedUnidade.observe(this, Observer {
            activity?.setResult(Activity.RESULT_OK)
            activity?.onBackPressed()
        })

        viewModel.getState().observe(viewLifecycleOwner, Observer { state ->

            when (state) {

                LeituraViewModel.State.IMPRESSAO -> {

                    with(PrintDialog(requireContext())) {
                        setPrintListener {

                            //TODO Deve imprimir a conta e assim que terminar mostrar dialog de forma de
                            //todo entrega da conta

                            var calculo  = Calculo(viewModel.leituraRepository.dataBase,
                                    viewModel.mUnidade.value!!,
                                    viewModel.leitura.value?.toInt() ?: 0)
                            calculo.calculaConta();


                            dismiss()

                            FormaEntregaDialog(requireContext()).let { deliveryDialog ->
                                deliveryDialog.setSaveListener {
                                    deliveryDialog.dismiss()
                                    viewModel.saveUnidade(it)

                                }
                            }.show()
                        }.show()
                    }
                }
            }
        })
    }

    private fun setupCardClicks() {
        debitos.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.debitos)
        }

        dicas.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.dicas)
        }

        ordem.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.ordemServico)
        }

        ocorrencia.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.ocorrencia)
        }

        dados.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.dadosCadastrais)
        }
        mapa.setOnClickListener {
            this.navigateToMapHome()
        }
    }

    private fun navigateToMapHome() {
        val ligacao: Ligacao = activity?.intent?.extras?.getParcelable(LeituraActivity.ARG_UNIDADE)
                ?: return
        context?.startActivity<MapHomeActivity>(MapHomeActivity.EXTRA_LIGACAO to ligacao)
    }
}