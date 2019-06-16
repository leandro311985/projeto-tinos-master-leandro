package br.com.iresult.gmfmobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.ui.login.LoginActivity
import br.com.iresult.gmfmobile.ui.main.estatistica.EstatisticaActivity
import br.com.iresult.gmfmobile.ui.main.impressao.backup.ImpressaoBackupFragment
import br.com.iresult.gmfmobile.ui.main.impressao.teste.ImpressaoTesteFragment
import br.com.iresult.gmfmobile.ui.main.impressao.ultimos.ImpressaoUltimosFragment
import br.com.iresult.gmfmobile.ui.main.pesquisa.PesquisaFragment
import br.com.iresult.gmfmobile.ui.main.tarefas.TarefasFragment
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.finish.FinishTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModel()
    var selectedButton: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.colorPrimary)
        toggle.syncState()

        drawer_layout.roteiros.setOnClickListener {
            goToFragment(TarefasFragment(), it)
        }

        drawer_layout.impressao_backup.setOnClickListener {
            goToFragment(ImpressaoBackupFragment(), it)
        }

        drawer_layout.impressao_teste.setOnClickListener {
            goToFragment(ImpressaoTesteFragment(), it)
        }

        drawer_layout.impressao_ultimos.setOnClickListener {
            goToFragment(ImpressaoUltimosFragment(), it)
        }

        drawer_layout.pesquisa.setOnClickListener {
            goToFragment(PesquisaFragment.newInstance(true), it)
        }

        drawer_layout.finalizar.setOnClickListener {
            val intent = Intent(this,FinishTaskActivity::class.java)
            startActivity(intent)
        }

        drawer_layout.roteiros.isSelected = true
        drawer_layout.sair.setOnClickListener {
            viewModel.logout()
        }

        goToFragment(TarefasFragment(), drawer_layout.roteiros)
        setupBindings()
    }

    private fun setupBindings() {

        viewModel.getUsuario().observe(this@MainActivity, Observer { usuario ->
            usuario?.let { user ->
                drawer_layout.username.text = "Usuário: ${user.siglaIdentificadora}"
                drawer_layout.usuario.text = user.nome
            }
        })

        viewModel.getState().observe(this@MainActivity, Observer { state ->

            when (state) {
                MainViewModel.State.LOGOUT -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        })
    }

    private fun goToFragment(fragment: BaseFragment, view: View) {

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit()

        selectedButton?.let { it.isSelected = false }
        selectedButton = view
        selectedButton?.isSelected = true
        toolbar_title.text = getString(fragment.getTitle())
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            SimpleDialog(this, getString(R.string.main_activity_title_dialog_close_app)).let {
                it.setupActionButton(getString(R.string.yes), getString(R.string.cancel)) { super.onBackPressed() }
                it.setDismissListener { it.dismiss() }
            }.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
