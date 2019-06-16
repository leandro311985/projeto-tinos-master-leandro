package br.com.iresult.gmfmobile.ui.main.pesquisa

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import br.com.iresult.gmfmobile.utils.bindTo
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.main.pesquisa.resultado.PesquisaResultadoFragment
import kotlinx.android.synthetic.main.fragment_pesquisa.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PesquisaFragment : BaseFragment() {

    val viewModel: PesquisaViewModel by sharedViewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_pesquisa

    override fun setupView() {

        btn_medidor.setOnClickListener { toggleSelected(it, PesquisaViewModel.SearchType.MEDIDOR) }
        btn_matricula.setOnClickListener { toggleSelected(it, PesquisaViewModel.SearchType.MATRICULA) }
        btn_nome.setOnClickListener { toggleSelected(it, PesquisaViewModel.SearchType.NOME) }
        btn_endereco.setOnClickListener { toggleSelected(it, PesquisaViewModel.SearchType.ENDERECO) }

        input_search
                .bindTo(viewModel.getSearchTerm(), viewLifecycleOwner)
                .addOnChangeListener { viewModel.setSearchTermValue(it) }

        input_search.setOnEditorActionListener { textView, i, keyEvent ->
            if (viewModel.getState().value == PesquisaViewModel.State.READY) {
                btn_search.performClick()
            }
            false
        }

        btn_search.setOnClickListener {
            goToResults()
        }
        
        if (arguments?.getBoolean(ARG_RESET_SEARCH) == true) {
            viewModel.initModelForSearch()
            arguments?.putBoolean(ARG_RESET_SEARCH, false)
        }

        viewModel.getSearchType().value?.let { toggleSelected(getViewByType(it), it) }

        setupBindings()
    }

    private fun goToResults() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right)
        fragmentTransaction.replace(R.id.content_frame, PesquisaResultadoFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun toggleSelected(view: View, type: PesquisaViewModel.SearchType) {
        btn_medidor.isSelected = false
        btn_matricula.isSelected = false
        btn_nome.isSelected = false
        btn_endereco.isSelected = false
        view.isSelected = true
        viewModel.setSearchTypeValue(type)
    }

    private fun setupBindings() {

        viewModel.getState().observe(this, Observer {
            onState(it)
        })
    }

    override fun getTitle(): Int = R.string.pesquisa_title

    private fun getViewByType(type: PesquisaViewModel.SearchType): View {
        return when (type) {
            PesquisaViewModel.SearchType.MEDIDOR -> btn_medidor
            PesquisaViewModel.SearchType.MATRICULA -> btn_matricula
            PesquisaViewModel.SearchType.NOME -> btn_nome
            PesquisaViewModel.SearchType.ENDERECO -> btn_endereco
        }
    }

    private fun onState(state: PesquisaViewModel.State?) {

        when (state) {

            PesquisaViewModel.State.READY -> {
                btn_search.isEnabled = true
            }

            PesquisaViewModel.State.LOADING -> {

            }

            PesquisaViewModel.State.ERROR -> {

            }

            PesquisaViewModel.State.SUCCESS -> {

            }

            else -> {
                btn_search.isEnabled = false
            }
        }
    }

    companion object {

        const val ARG_RESET_SEARCH = "ARG_RESET"

        @JvmStatic
        fun newInstance(reset: Boolean) = PesquisaFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_RESET_SEARCH, reset)
            }
        }
    }
}