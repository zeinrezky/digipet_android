package com.stellkey.android.view.carer.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.databinding.ItemProfileKidRewardBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.RewardModel

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version ActiveRewardAdapter, v 0.1 11/01/23 17.41 by Nicolas Manurung
 */
class ActiveRewardAdapter(
    val activeRewardList: ArrayList<RewardModel>
) : Adapter<ActiveRewardAdapter.ActiveRewardViewHolder>() {

    inner class ActiveRewardViewHolder(private val binding: ItemProfileKidRewardBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: RewardModel) {
            binding.ivProfileReward.loadImage(item.icon)
            binding.tvRewardType.text = "${item.star_cost}-star reward"
            binding.tvRewardName.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveRewardViewHolder =
        ActiveRewardViewHolder(
            ItemProfileKidRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = activeRewardList.size

    override fun onBindViewHolder(holder: ActiveRewardViewHolder, position: Int) {
        holder.bind(activeRewardList[position])
    }

    fun selectReward(position: Int) = activeRewardList[position]
}