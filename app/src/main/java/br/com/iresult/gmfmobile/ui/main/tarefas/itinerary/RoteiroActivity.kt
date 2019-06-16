package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Address
import br.com.iresult.gmfmobile.model.bean.Ligacao
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.ui.main.impressao.Impressao
import br.com.iresult.gmfmobile.ui.main.impressao.ImpressaoDialog
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.filter.RoutingFilterDialog
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.finish.FinishTaskActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.MapsHomeActivity
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraActivity
import br.com.iresult.gmfmobile.ui.widget.HomeStatus
import br.com.iresult.gmfmobile.ui.widget.HomeStatusView
import br.com.iresult.gmfmobile.utils.BaseAdapter
import kotlinx.android.synthetic.main.activity_roteiro.*
import kotlinx.android.synthetic.main.rua_item.view.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by victorfernandes on 24/02/19.
 */
class RoteiroActivity : AppCompatActivity() {

    val viewModel : RoteiroViewModel by viewModel()
    private var pending: Boolean = true
    private var finished: Boolean = true
    private var reversed: Boolean = false
    private var imprimir: Impressao? = null
    private val adapter = BaseAdapter(R.layout.rua_item) { address: Address, itemView ->

        itemView.nomeRua.text = address.nome
        itemView.cepRua.text = address.formattedAddress()
        itemView.setOnClickListener {
            this.toggleItem(itemView)
            viewModel.fetchHouses(address.nome)
        }
        itemView.rvHome.layoutManager = GridLayoutManager(itemView.context, 3, RecyclerView.VERTICAL, false)
        itemView.rvHome.adapter = BaseAdapter(R.layout.item_home) { home: Ligacao, homeStatusView ->

            (homeStatusView as HomeStatusView).let { homeView ->
                homeView.tvNumberHome.text = home.numero
                homeView.setupLayout(home.status?.let { status -> HomeStatus.valueOf(status) } ?: run { HomeStatus.NONE })
                homeView.setOnClickListener { this.navigateToReaderAcitivty(address, home) }
            }

        }.also { adapter ->
            viewModel.ligacao.observe(this, Observer {
                adapter.setItems(viewModel.formattedHouses(it, pending, finished, reversed))
            })
        }
    }

    companion object {
        const val ARG_ROTEIRO = "ARG_ROTEIRO"
        const val REQUEST_CODE_UPDATED_HOUSE = 323
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roteiro)

        intent.extras?.getString(ARG_ROTEIRO)?.let { roteiro ->
            tvTitle.text = roteiro
            viewModel.fetchAdressesByFileName(roteiro)
        }

        ivPreviewMap.setOnClickListener {
            startActivity<MapsHomeActivity>(MapsHomeActivity.EXTRA_BUNDLE_TASK_NAME to tvTitle.text.toString())
        }

        btBack.setOnClickListener { finish() }
        setupRecyclerView()
        ivFinishTask.setOnClickListener { this.navigateToFinishTask() }

        viewModel.errorMessage.observe(this, Observer { error ->
            val dialog = SimpleDialog(this  , error)
            dialog.setupCloseButton(getString(R.string.ok))
            dialog.show()

        })

        viewModel.roteiro.observe(this, Observer { roteiro ->
            ruas_total.text = "Endereços (${roteiro?.totalRuas})"
            ruas_restantes.text = "Faltam (${roteiro?.totalRuas})"
            casas_total.text = "Imóveis (${roteiro?.totalCasas})"
            casas_restantes.text = "Faltam (${roteiro.totalCasas})"
            porcentagem.text = "0%"
        })

        float_roteiro.setOnClickListener {
            RoutingFilterDialog(this, pending, finished, reversed).also { dialog ->
                dialog.onConfirm = { pending, finished, reversed ->
                    this.pending = pending
                    this.finished = finished
                    this.reversed = reversed
                    adapter.setItems(viewModel.address.value ?: emptyList())
                }
            }.show()
        }

        float_roteiro_imprimir.setOnClickListener {
            imprimir = Impressao(viewModel.dataBase)
            ImpressaoDialog(this).also { dialog ->
                dialog.onConfirm = {imprimir50, imprimiBackup ->
                    var impressoraConectada: Boolean =  true
                    if (imprimir50){
                        impressoraConectada = imprimir?.imprimir50(viewModel.roteiro.value?.nome!!)!!
                    } else if (imprimiBackup) {
                        impressoraConectada = imprimir?.imprimirBackUp(viewModel.roteiro.value?.nome!!)!!
                    }
                    if (!impressoraConectada){
                        viewModel.mErrorMessage.value  = "Impressora não configrada corretamente, favor verificar."
                    }

                }
            }.show()
        }

        viewModel.progess.observe(this, Observer {
            porcentagem.text = "$it%"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progresso.setProgress(it, true)
            } else {
                progresso.progress = it
            }
        })
    }

    private fun resetFilter() {
        this.reversed = false
        this.pending = true
        this.finished = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {

            REQUEST_CODE_UPDATED_HOUSE -> {
                if (resultCode == Activity.RESULT_OK) {
                    this.resetFilter()
                    viewModel.fetchAdressesByFileName(tvTitle.text.toString())
                }
            }
            else -> {

            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter.also {
            viewModel.address.observe(this, Observer { addresses ->
                it.setItems(addresses)
            })
        }
    }

    private fun toggleItem(view: View) {

        viewModel.address.value?.forEachIndexed { index, _ ->
            val container: View? = recyclerView.getChildAt(index)
            if (container?.isSelected == true && container != view) {
                container.isSelected = false
                container.rvHome?.visibility = View.GONE
            }
        }

        if(view.rvHome.visibility == View.GONE) {
            view.rvHome.visibility = View.VISIBLE
            view.isSelected = true
        } else {
            view.rvHome.visibility = View.GONE
            view.isSelected = false
        }
    }

    private fun navigateToFinishTask() {
        startActivity(Intent(this, FinishTaskActivity::class.java))
    }

    private fun navigateToReaderAcitivty(address: Address, home: Ligacao) {
        val intent = Intent(this, LeituraActivity::class.java).let {
            it.putExtra(LeituraActivity.ARG_UNIDADE, home)
            it.putExtra(LeituraActivity.ARG_RUA, address)
            it.putExtra(LeituraActivity.ARG_ROTEIRO, viewModel.roteiro.value?.nome)
        }
        startActivityForResult(intent, REQUEST_CODE_UPDATED_HOUSE)
    }
}