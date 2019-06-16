package br.com.iresult.gmfmobile.ui.main.estatistica

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_estatistica.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstatisticaActivity : AppCompatActivity() {

    val viewModel: EstatisticaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_estatistica)
        btn_back.setOnClickListener { finish() }
    }
}