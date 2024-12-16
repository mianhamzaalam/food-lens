package com.driven.foodlens.Adapters.AiModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.DirectionLayoutBinding
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AiModel.AlternativeIngredient

class DirectionAdapter :RecyclerView.Adapter<DirectionAdapter.viewHolder>() {

    class viewHolder(val binding:DirectionLayoutBinding):RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return newItem==oldItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return newItem==oldItem
        }

    }

    var differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(DirectionLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val direction = differ.currentList[position]
        holder.binding.textView23.text = direction.toString()
    }
}