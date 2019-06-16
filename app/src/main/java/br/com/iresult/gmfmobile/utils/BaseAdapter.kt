package br.com.iresult.gmfmobile.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic class for adapter
 * Created by daflecardoso on 07/05/19.
 */
open class BaseAdapter<T>(@LayoutRes val view: Int,
                          val onBind:(T, View) -> Unit): RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    private var items: List<T> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(view, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            onBind(items[position], holder.itemView)

    fun setItems(items: List<T>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}

