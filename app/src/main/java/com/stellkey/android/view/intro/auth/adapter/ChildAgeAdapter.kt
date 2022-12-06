package com.stellkey.android.view.intro.auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.databinding.ItemChildAgeBinding
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.ChildAgeModel


class ChildAgeAdapter(private val ageList: List<ChildAgeModel>) :
    RecyclerView.Adapter<ChildAgeAdapter.ChildAgeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAgeViewHolder {
        return ChildAgeViewHolder(
            ItemChildAgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ageList.size
    }

    override fun onBindViewHolder(holder: ChildAgeViewHolder, position: Int) {
        holder.bind(ageList[position])
    }

    fun updateSelectedItem(pos: Int) {
        ageList.forEach {
            if (it.isSelected)
                it.isSelected = !it.isSelected
        }
        ageList[pos].isSelected = !ageList[pos].isSelected
        //notifyItemChanged(pos)
        notifyDataSetChanged()
    }

    inner class ChildAgeViewHolder(private val binding: ItemChildAgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChildAgeModel) {
            binding.clBackgroundActiveItem.isVisible = item.isSelected
            binding.tvChildAge.textOrNull(item.index.toString())
        }
    }

}