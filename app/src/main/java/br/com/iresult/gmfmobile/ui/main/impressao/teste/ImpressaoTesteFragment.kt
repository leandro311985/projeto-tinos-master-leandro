package br.com.iresult.gmfmobile.ui.main.impressao.teste

import android.content.Intent
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_impressao_teste.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImpressaoTesteFragment : BaseFragment() {

    val viewModel: ImpressaoTesteViewModel by viewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_impressao_teste

    override fun setupView() {

        btn_print.setOnClickListener {
          //  Toast.makeText(context, "indisponivel", Toast.LENGTH_SHORT).show()
        }

        btn_cancel.setOnClickListener{
            val intent= Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getTitle(): Int = R.string.impressao_teste_title

}