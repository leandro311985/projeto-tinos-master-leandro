package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.tips

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.iresult.gmfmobile.R
import kotlinx.android.synthetic.main.dicas_item.view.*

class DicasAdapter(val dicas: List<String>) : RecyclerView.Adapter<DicasAdapter.DicasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): DicasViewHolder {
        return DicasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dicas_item,
                parent,
                false))
    }

    override fun getItemCount(): Int = dicas.size

    override fun onBindViewHolder(holder: DicasViewHolder, position: Int) {
        holder.bind(dicas[position])
    }

    class DicasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dica: String) = with(itemView) {

            itemView.dica.text = dica

        }

    }

}