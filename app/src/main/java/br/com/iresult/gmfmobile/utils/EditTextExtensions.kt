package br.com.iresult.gmfmobile.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Created by victorfernandes on 14/10/18.
 */
fun EditText.addOnChangeListener(listener: (String) -> (Unit)) {

    this.addTextChangedListener(object : TextWatcher {

        var currentString = ""

        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != currentString) {
                listener.invoke(p0.toString())
            }
            currentString = p0.toString()
        }
    })
}

fun EditText.bindTo(data: LiveData<String>, lifecyleOwner: LifecycleOwner): EditText {
    data.observe(lifecyleOwner, Observer { value ->
        if (value != this.text.toString()) {
            this.setText(value ?: "")
            this.setSelection(this.length())
        }
    })
    return this
}