package com.stellkey.android.view.child.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidAchievementBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.BadgesModel

class BadgeAdapter(
    var listOfBadge: ArrayList<BadgesModel>
) : Adapter<BadgeAdapter.BadgeViewHolder>() {

    inner class BadgeViewHolder(private val binding: ItemKidAchievementBinding) :
        ViewHolder(binding.root) {
        fun bind(item: BadgesModel) {
            binding.ivAchievementStatus.loadImage(item.icon)
            binding.tvAchievementName.text = item.description
            if (item.isOwned) {
                binding.ivAchievementStatus.clearColorFilter()
            } else {
                binding.ivAchievementStatus.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.transparent_black
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder =
        BadgeViewHolder(
            ItemKidAchievementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listOfBadge.size

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        holder.bind(listOfBadge[position])
    }
}