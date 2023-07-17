package com.mymuslem.sarrawi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.R
import com.mymuslem.sarrawi.models.Letters
import com.mymuslem.sarrawi.models.Zekr

class VPagerAdapter(private var zeker_list:List<Zekr>): RecyclerView.Adapter<VPagerAdapter.Pager2View>() {

    inner class Pager2View(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv_zeker:TextView=itemView.findViewById(R.id.tv_zeker)
        var tv_count:TextView=itemView.findViewById(R.id.tv_count)
        var tv_D:TextView=itemView.findViewById(R.id.tv_d)
        var btn_count:Button=itemView.findViewById(R.id.button1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2View {
       return Pager2View(LayoutInflater.from(parent.context).inflate(R.layout.item_pager,parent,false))
    }

    override fun onBindViewHolder(holder: Pager2View, position: Int) {
        val zekr: Zekr = zeker_list.get(position)
//        holder.title.setText(m.getName())
        holder.tv_zeker.text = zekr.Description
        holder.tv_count.text = zekr.couner
        holder.tv_D.text = zekr.hint


    }

    override fun getItemCount(): Int {
        return zeker_list.size
    }

    fun updateData(newZekerList: List<Zekr>) {
        zeker_list = newZekerList
        notifyDataSetChanged()
    }
}