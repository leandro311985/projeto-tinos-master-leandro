package br.com.iresult.gmfmobile.ui.main.impressao.ultimos

import android.content.Intent
import android.widget.Toast
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.main.MainActivity
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import kotlinx.android.synthetic.main.fragment_impressao_ultimos.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImpressaoUltimosFragment : BaseFragment() {

    val viewModel: ImpressaoUltimosViewModel by sharedViewModel()
    val viewModel2: LeituraViewModel by sharedViewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_impressao_ultimos

    override fun setupView() {
        btn_cancel.setOnClickListener {
            val intent= Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        btn_print.setOnClickListener {

            var stImp: Boolean = viewModel.imprimeUltimos50()
            if (stImp){
                Toast.makeText(context,"OKOKOKOK", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"OKOKOKOK", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getTitle(): Int = R.string.impressao_ultimos_title
}