package br.com.iresult.gmfmobile.ui.main.impressao.backup

import android.content.Intent
import android.widget.Toast
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.base.BaseFragment
import br.com.iresult.gmfmobile.ui.base.PrintDialog
import br.com.iresult.gmfmobile.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_impressao_backup.*
import kotlinx.android.synthetic.main.fragment_impressao_backup.btn_cancel
import kotlinx.android.synthetic.main.fragment_impressao_backup.btn_print
import kotlinx.android.synthetic.main.fragment_impressao_ultimos.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImpressaoBackupFragment : BaseFragment() {

    val viewModel: ImpressaoBackupViewModel by viewModel()

    override fun getLayoutResource(): Int = R.layout.fragment_impressao_backup

    override fun setupView() {


        btn_cancel.setOnClickListener {
            val intent= Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        btn_print.setOnClickListener {
            Toast.makeText(context,"indisponivel", Toast.LENGTH_SHORT).show()

        }

    }

    override fun getTitle(): Int = R.string.impressao_backup_title

}