package br.com.iresult.gmfmobile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.iresult.gmfmobile.R
//import br.com.iresult.gmfmobile.model.bean.Debito
import kotlinx.android.synthetic.main.debito_item.view.*
import java.text.NumberFormat

//class DebitoAdapter(val debitos: List<Debito>, val selectedListener: (List<Debito>) -> (Unit)) : androidx.recyclerview.widget.RecyclerView.Adapter<DebitoAdapter.DebitoViewHolder>(){
//
//    val selectedDebitos = ArrayList<Debito>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, type: Int): DebitoViewHolder {
//        return DebitoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.debito_item,
//                parent,
//                false))
//    }
//
//    override fun getItemCount(): Int = debitos.size
//
//    override fun onBindViewHolder(holder: DebitoViewHolder, position: Int) {
//        holder.bind(debitos[position])
//    }
//
//    inner class DebitoViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
//
//        fun bind(debito: Debito) = with(itemView) {
//
//            data.text = debito.date
//            valor.text = NumberFormat.getCurrencyInstance().format(debito.valor)
//            vencimento.text = debito.vencimento
//
//            itemView.isSelected = selectedDebitos.contains(debito)
//
//            itemView.setOnClickListener {
//
//                if(selectedDebitos.contains(debito)) {
//                    selectedDebitos.remove(debito)
//                } else {
//                    selectedDebitos.add(debito)
//                }
//
//                selectedListener.invoke(selectedDebitos)
//                notifyDataSetChanged()
//
//            }
//
//        }
//
//    }
//
//}