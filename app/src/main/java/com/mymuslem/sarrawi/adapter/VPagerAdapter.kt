package com.mymuslem.sarrawi.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.R
import com.mymuslem.sarrawi.db.viewModel.SettingsViewModel
import com.mymuslem.sarrawi.models.Zekr


class VPagerAdapter(val con: Context, private var zeker_list:List<Zekr>, val fragment: Fragment, var fontFamily: Typeface?=null): RecyclerView.Adapter<VPagerAdapter.Pager2View>() {
    var counterr = 0
    var settingsViewModel = ViewModelProvider(fragment).get(SettingsViewModel::class.java)
    private var selectedTypeface: Typeface = Typeface.DEFAULT // نوع الخط الافتراضي

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
        holder.tv_zeker.textSize = settingsViewModel.fontSize.toFloat()
        holder.tv_count.text = zekr.couner
        holder.tv_D.text = zekr.hint
        holder.tv_zeker.typeface = selectedTypeface
        fontFamily?.let {
            holder.tv_zeker.typeface = it
        }
//        holder.btn_count.setOnClickListener{
//            counterr++
//            holder.btn_count.setText(Integer.toString(counterr))
//            holder.btn_count.setTextColor(Color.BLUE)
//            0
//
//        }

        holder.btn_count.setOnClickListener (View.OnClickListener { v ->
            if (v === holder.btn_count) {
                counterr++
                //textTitle.setText(Integer.toString(counter));
                holder.btn_count.setText(Integer.toString(counterr))
                //scoreText.setBackgroundColor(Color.CYAN);
                holder.btn_count.setTextColor(Color.BLUE)
                0
            }
        })


    }

    override fun getItemCount(): Int {
        return zeker_list.size
    }

    fun updateData(newZekerList: List<Zekr>) {
        zeker_list = newZekerList
        notifyDataSetChanged()
    }

    fun setFont(font: Typeface?) {
        this.fontFamily = font
        notifyDataSetChanged()
    }
}