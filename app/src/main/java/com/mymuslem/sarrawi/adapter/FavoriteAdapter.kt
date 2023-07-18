package com.mymuslem.sarrawi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.R
import com.mymuslem.sarrawi.databinding.FavoriteDesignBinding
import com.mymuslem.sarrawi.models.FavoriteModel

class FavoriteAdapter(val con: Context): RecyclerView.Adapter<FavoriteAdapter.MyHolder>() {

    var del_fav: ((fav:FavoriteModel) -> Unit)? = null // pass favorite item on click
//    var onItemClick: ((Int) -> Unit)? = null
    var onItemClick: ((FavoriteModel) -> Unit)? = null


    @SuppressLint("NotifyDataSetChanged")
    inner class MyHolder(val binding:FavoriteDesignBinding): RecyclerView.ViewHolder(binding.root) {

        init{


            binding.root.setOnClickListener{
//                onItemClick?.invoke(zeker_fav_list[layoutPosition].ID?:0)
                onItemClick?.invoke(zeker_fav_list[layoutPosition])
            }

            binding.imgFavF.setOnClickListener {

                del_fav?.invoke(zeker_fav_list[layoutPosition])
            }
        }




    }

    private val diffCallback = object : DiffUtil.ItemCallback<FavoriteModel>(){
        override fun areItemsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return oldItem.ID == newItem.ID
        }

        override fun areContentsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var zeker_fav_list: List<FavoriteModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FavoriteDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val current_zeker_fav_list = zeker_fav_list[position]
        holder.binding.apply {
            gtitleDoaa.text = current_zeker_fav_list.Name
        }
    }

    override fun getItemCount(): Int {
        return zeker_fav_list.size
    }
}