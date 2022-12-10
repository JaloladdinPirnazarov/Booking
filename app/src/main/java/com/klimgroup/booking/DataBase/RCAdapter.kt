package com.klimgroup.booking.DataBase

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klimgroup.booking.R

class RCAdapter(
    private val tablesList:ArrayList<TableItems>,
    private var actions: Actions
): RecyclerView.Adapter<RCAdapter.Holder>() {

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private val tvNumber: TextView = itemView.findViewById(R.id.tvNumber)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvFrom: TextView = itemView.findViewById(R.id.tvFrom)
        private val tvTo: TextView = itemView.findViewById(R.id.tvTo)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)

        @SuppressLint("SetTextI18n")
        fun fill(table:TableItems){
            tvNumber.text = "№ ${table.number}"
            tvPrice.text = "${table.price}$"
            tvFrom.text = "from: ${table.from}"
            tvTo.text = "to: ${table.to}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.rc_table_item,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val table = tablesList[position]
        holder.fill(table)
        holder.itemView.setOnClickListener { actions.click(table) }
        holder.imgDelete.setOnClickListener { actions.delete(table.id) }
    }

    override fun getItemCount(): Int = tablesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newTablesList:ArrayList<TableItems>){
        tablesList.clear()
        tablesList.addAll(newTablesList)
        notifyDataSetChanged()
    }
}