package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.RewardModel

class RecommendedRewardAdapter(
    context: Context,
    list: ArrayList<RewardModel>,
    private val listener: Listener
) : RecyclerView.Adapter<RecommendedRewardAdapter.RewardListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: RewardModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_recommended_reward, parent, false)
        return RewardListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun updateSelectedItem(pos: Int) {
        itemList.forEach {
            if (it.isSelected)
                it.isSelected = !it.isSelected
        }
        itemList[pos].isSelected = !itemList[pos].isSelected

        //notifyItemChanged(pos)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RewardListViewHolder, position: Int) {
        holder.apply {
            if (itemList[position].isSelected)
                cvKidReward.setCardBackgroundColor(
                    ContextCompat.getColor(
                        contexts,
                        R.color.light_blue
                    )
                )
            else
                cvKidReward.setCardBackgroundColor(ContextCompat.getColor(contexts, R.color.white))

            cvKidReward.setOnClickListener {
                updateSelectedItem(position)
                listener.onItemClicked(itemList[position])
            }
            ivReward.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 100)
            tvRewardName.textOrNull = itemList[position].title
            tvStarAmount.textOrNull = itemList[position].star_cost.toString()
        }
    }

    inner class RewardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvKidReward: MaterialCardView = itemView.findViewById(R.id.cvKidReward)
        val ivReward: ImageView = itemView.findViewById(R.id.ivReward)
        val tvRewardName: TextView = itemView.findViewById(R.id.tvRewardName)
        val tvStarAmount: TextView = itemView.findViewById(R.id.tvStarAmount)
    }
}