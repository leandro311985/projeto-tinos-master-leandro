package br.com.iresult.gmfmobile.ui.adapters

import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.ui.PreviewImageActivty
import br.com.iresult.gmfmobile.ui.base.SimpleDialog
import br.com.iresult.gmfmobile.utils.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.imagem_item.view.*
import org.jetbrains.anko.startActivity
import java.io.File

class ImageAdapter(handle: (File) -> Unit): BaseAdapter<File>(R.layout.imagem_item, { image, itemView ->
    Glide.with(itemView).load(image).fitCenter().centerCrop().into(itemView.imagem)
    itemView.setOnClickListener {
        it.context.startActivity<PreviewImageActivty>(PreviewImageActivty.EXTRA_FILE_IMAGE to image)
    }
    itemView.setOnLongClickListener {
        SimpleDialog(itemView.context, "Deseja realmente remover a imagem?").let { dialog ->
            dialog.setupActionButton(itemView.context.getString(R.string.yes), itemView.context.getString(R.string.cancel)) {
                handle(image)
                dialog.dismiss()
            }.show()
        }
        true
    }
})