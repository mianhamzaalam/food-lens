package com.driven.foodrecipeapp.Adapters.BannerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.BannerLayoutBinding
import com.driven.foodrecipeapp.Model.Banner

class ViewPagerAdapter(private val list: List<Banner>):RecyclerView.Adapter<ViewPagerAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(BannerLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.imageView4.setImageResource(list[position].image)
        holder.binding.Category.text = list[position].text
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class viewHolder(val binding: BannerLayoutBinding):RecyclerView.ViewHolder(binding.root)
}