package br.com.iresult.gmfmobile.ui.main.pesquisa.resultado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.iresult.gmfmobile.R
import br.com.iresult.gmfmobile.model.bean.Ligacao
import kotlinx.android.synthetic.main.activity_leitura.view.endereco
import kotlinx.android.synthetic.main.pesquisa_resultado_item.view.*

class PesquisaResultadoAdapter(private val resultados: List<Ligacao>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<PesquisaResultadoAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.pesquisa_resultado_item, parent, false))
    }

    override fun getItemCount(): Int = resultados.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(resultados[position])
    }

    class ItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(ligacao: Ligacao) = with(itemView) {

            itemView.endereco.text = context.getString(R.string.endereco, ligacao.rua, ligacao.numero)

            applyConditionalValue(itemView.bairro, ligacao.bairro?.let { it })
            applyConditionalValue(itemView.complemento, ligacao.descricaoComplemento?.let { it })
            applyConditionalValue(itemView.cep, ligacao.cep?.let { it })

            applyConditionalValue(itemView.morador, ligacao.nomeCliente?.let { context.getString(R.string.pesquisa_morador, it) })
            applyConditionalValue(itemView.codigo_casa, context.getString(R.string.pesquisa_codigo_casa, ligacao.numReg))
            applyConditionalValue(itemView.hidrometro, ligacao.numeroMedidor?.let { context.getString(R.string.pesquisa_hidrometro, it) })
        }

        private fun applyConditionalValue(textView: TextView, value: String?) {
            if (value != null) {
                textView.text = value
                textView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                textView.layoutParams.height = 0
            }
        }
    }
}