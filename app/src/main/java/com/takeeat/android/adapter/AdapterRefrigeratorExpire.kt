package com.takeeat.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.takeeat.android.data.model.RefrigeratorIngredientData
import com.takeeat.android.databinding.RecycleIngredientHorizontalBinding

class AdapterRefrigeratorExpire(private val listData: ArrayList<RefrigeratorIngredientData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
        return PartViewHolder(RecycleIngredientHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PartViewHolder -> {
                holder.bind(listData[position],position)
            }
        }
    }

    inner class PartViewHolder(private val binding: RecycleIngredientHorizontalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: RefrigeratorIngredientData, position: Int){
            binding.text.text = data.name
        }
    }

}