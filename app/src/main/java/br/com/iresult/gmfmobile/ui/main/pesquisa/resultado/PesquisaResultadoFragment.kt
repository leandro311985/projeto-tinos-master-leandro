package br.com.iresult.gmfmobile.ui.main.pesquisa.resultado

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import br.com.iresult.gmfmobile.utils.bindTo
import br.com.iresult.gmfmobile.utils.hideKeyboard
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.main.pesquisa.PesquisaViewModel
import kotlinx.android.synthetic.main.fragment_pesquisa.input_search
import kotlinx.android.synthetic.main.fragment_pesquisa_resultado.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PesquisaResultadoFragment : BaseFragment() {

    val viewModel: PesquisaViewModel by sharedViewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_pesquisa_resultado

    override fun setupView() {

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        setupBindings()

        input_search
                .bindTo(viewModel.getSearchTerm(), viewLifecycleOwner)
                .addOnChangeListener { viewModel.setSearchTermValue(it) }

        input_search.setOnEditorActionListener { textView, i, keyEvent ->
            if (viewModel.getState().value == PesquisaViewModel.State.READY) {
                viewModel.search()
                hideKeyboard()
            }
            false
        }

        btn_back.setOnClickListener { fragmentManager?.popBackStack() }

        viewModel.getSearchType().value?.let { type ->
            viewModel.getSearchTerm().value?.let { term ->
                viewModel.initModelForResult(type, term)
                title.text = getString(R.string.pesquisa_search_type, type.title)
            }
        }
    }

    override fun getTitle(): Int = R.string.pesquisa_title

    fun setupBindings() {

        viewModel.getResultados().observe(this@PesquisaResultadoFragment, Observer {
            recyclerView.adapter = it?.let { resultados ->
                PesquisaResultadoAdapter(resultados)
            }
        })

        viewModel.getState().observe(this, Observer {
            onState(it)
        })
    }

    private fun onState(state: PesquisaViewModel.State?) {

        when (state) {

            PesquisaViewModel.State.LOADING -> {
                no_results.visibility = View.GONE
            }

            PesquisaViewModel.State.SUCCESS -> {
                viewModel.getResultados().value?.let {
                    no_results.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                }
            }
            else -> {
            }
        }
    }
}