package com.stellkey.android.view.child.reward.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidRewardBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.KidRewardRedemption
import com.stellkey.android.util.AppPreference

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version ChildRewardAdapter, v 0.1 16/01/23 20.41 by Nicolas Manurung
 */
class ChildRewardAdapter(
    var listRewards: List<KidRewardRedemption>,
    val listener: Listener
) : Adapter<ChildRewardAdapter.ChildRewardViewHolder>() {

    interface Listener {

        fun onRewardClickedForRedeemed(data: KidRewardRedemption)
    }

    inner class ChildRewardViewHolder(private val binding: ItemKidRewardBinding) :
        ViewHolder(binding.root) {
        private val localeLanguage = AppPreference.getKidLocale() == "en"

        @SuppressLint("SetTextI18n")
        fun bind(item: KidRewardRedemption) {
            binding.ivReward.loadImage(item.icon)
            binding.tvReward.text = if (localeLanguage && !item.isGlobal) item.title else item.titleFr
            if (item.star_balance >= item.star_cost) {
                binding.ivReward.clearColorFilter()
                binding.clRewardUnavailable.isVisible = false
                binding.clRewardAvailable.isVisible = true
            } else {
                binding.ivReward.setColorFilter(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.transparent_black
                    )
                )
                binding.clRewardUnavailable.isVisible = true
                binding.clRewardAvailable.isVisible = false
                binding.piStarProgress.progress =
                    ((item.star_balance.toFloat() / item.star_cost.toFloat()) * 100).toInt()
                binding.tvStar.text = "${item.star_balance}/${item.star_cost}"
            }

            binding.clRewardAvailable.setOnClickListener {
                listener.onRewardClickedForRedeemed(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildRewardViewHolder =
        ChildRewardViewHolder(
            ItemKidRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = listRewards.size

    override fun onBindViewHolder(holder: ChildRewardViewHolder, position: Int) {
        holder.bind(listRewards[position])
    }
}