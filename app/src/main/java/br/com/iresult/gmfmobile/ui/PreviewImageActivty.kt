package br.com.iresult.gmfmobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.iresult.gmfmobile.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_preview_image_activty.*
import kotlinx.android.synthetic.main.imagem_item.view.*
import java.io.File

class PreviewImageActivty : AppCompatActivity() {

    companion object {

        const val EXTRA_FILE_IMAGE = "EXTRA_FILE_IMAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_image_activty)
        val image = intent.extras?.get(EXTRA_FILE_IMAGE) as? File
        Glide.with(this).load(image).into(ivPreview)
    }
}
