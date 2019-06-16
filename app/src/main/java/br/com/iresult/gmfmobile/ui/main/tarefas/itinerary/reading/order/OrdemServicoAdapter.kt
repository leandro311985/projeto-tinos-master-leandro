package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.ServicoLeitura
import kotlinx.android.synthetic.main.servicos_leituras_item.view.*

class OrdemServicoAdapter(val servicos: List<ServicoLeitura>, val listener: (ServicoLeitura) -> (Unit)) : RecyclerView.Adapter<OrdemServicoAdapter.ServicoLeituraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicoLeituraViewHolder {
            return ServicoLeituraViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.servicos_leituras_item,
                    parent,
                    false))
    }

    override fun getItemCount(): Int = servicos.count()

    override fun onBindViewHolder(holder: ServicoLeituraViewHolder, position: Int) {
        holder.bind(servicos[position])
    }

    inner class ServicoLeituraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(servico: ServicoLeitura) = with(itemView) {

            text.text = servico.rubrica.descricaoRubrica

            itemView.setOnClickListener {

                listener.invoke(servico)

            }

        }

    }

}