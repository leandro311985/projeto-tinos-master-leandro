package br.com.iresult.gmfmobile.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.iresult.gmfmobile.utils.hideKeyboard

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    abstract fun getLayoutResource(): Int
    abstract fun setupView()
    abstract fun getTitle(): Int
}