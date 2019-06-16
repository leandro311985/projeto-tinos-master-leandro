package br.com.iresult.gmfmobile.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.login.LoginActivity
import br.com.iresult.gmfmobile.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity()  {

    val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fade_in: Animation = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        val fade_out: Animation = AnimationUtils.loadAnimation(this,R.anim.fade_out)

        tinos.startAnimation(fade_in)
        gmf.startAnimation(fade_in)

        fade_in.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                tinos.startAnimation(fade_out)
                gmf.startAnimation(fade_out)
            }
        })

        viewModel.user.observe(this, Observer {
            if (it != null) {
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
        })

        Handler().postDelayed({
            viewModel.getUser()
        }, 3000)
    }
}