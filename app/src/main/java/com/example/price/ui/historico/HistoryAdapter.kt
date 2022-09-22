package com.example.price.ui.historico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.price.Dolar
import com.example.price.R

class HistoryAdapter(private var dollarArrayList: MutableList<Dolar>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.table_row_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tvFecha.text = dollarArrayList[i].fecha
        viewHolder.tvVenta.text = dollarArrayList[i].venta.toString()
        viewHolder.tvCompra.text = dollarArrayList[i].compra.toString()
    }

    override fun getItemCount(): Int {
        return dollarArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFecha: TextView = itemView.findViewById(R.id.tv_col1)
        val tvVenta: TextView = itemView.findViewById(R.id.tv_col2)
        val tvCompra: TextView = itemView.findViewById(R.id.tv_col3)


    }
}