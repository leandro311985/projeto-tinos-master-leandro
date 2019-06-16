package br.com.iresult.gmfmobile.ui.login

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import br.com.iresult.gmfmobile.BuildConfig
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.MainActivity
import br.com.iresult.gmfmobile.utils.addOnChangeListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.usuario
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by victorfernandes on 04/12/18.
 */
class LoginActivity : AppCompatActivity() {

    val viewModel : LoginViewModel by viewModel()
    var progress : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        KeyboardVisibilityEvent.setEventListener(this) {
            keyboarOpened(it)
        }

        entrar.setOnClickListener {
            if (senha.text.toString().equals("")&& usuario.text.toString().equals("")){
                Snackbar.make(constraint, "Existem Campos Usuário ou senha em branco", Snackbar.LENGTH_LONG).show()
            }else
                viewModel.doLogin()

        }

        version.text = BuildConfig.VERSION_NAME

        setupBindings()

    }

    fun setupBindings() {

        usuario.addOnChangeListener {
            viewModel.setUsuarioValue(it)
        }

        senha.addOnChangeListener {
            viewModel.setSenhaValue(it)
        }

        viewModel.getUsuario().observe(this, Observer {
            if(usuario.text.toString() != it) {
                usuario.setText(it)
            }
        })

        viewModel.getSenha().observe(this, Observer {
            if(senha.text.toString() != it) {
                senha.setText(it)
            }
        })

        viewModel.getState().observe(this, Observer {
            onState(it)
        })

        viewModel.getData().observe(this, Observer {

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            finish()
            startActivity(intent)

        })

    }

    fun onState(state: LoginViewModel.State?) {

        when(state) {

            LoginViewModel.State.LOADING -> {

                progress = ProgressDialog(this@LoginActivity)
                progress?.setMessage("Sincronizando dados...")
                progress?.show()

            }

            LoginViewModel.State.ERROR -> {

                Snackbar.make(constraint, "Usuário ou senha inválidos", Snackbar.LENGTH_LONG).show()
                progress?.dismiss()

            }

            LoginViewModel.State.SUCCESS -> {

                progress?.dismiss()

            }

        }

    }

    fun keyboarOpened(opened: Boolean) {
        logo.visibility = if(opened) View.GONE else View.VISIBLE
    }

}