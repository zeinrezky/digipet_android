package com.stellkey.android.view.carer.account.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.NewSubscriptionModel

class SubscriptionAdapter(
    context: Context,
    list: ArrayList<NewSubscriptionModel>,
    private val listener: Listener
) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: NewSubscriptionModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_premium_upgrade, parent, false)
        return SubscriptionViewHolder(view)
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

    fun resetSelectedItem() {
        itemList.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.apply {
            if (itemList[position].isSelected) {
                cvPremiumUpgrade.setCardBackgroundColor(
                    ContextCompat.getColor(
                        contexts,
                        R.color.colorPrimary
                    )
                )
                tvSubscriptionDuration.setTextColor(ContextCompat.getColor(contexts, R.color.white))
                tvSubscriptionDurationType.setTextColor(ContextCompat.getColor(contexts, R.color.white))
                tvSubscriptionPrice.setTextColor(ContextCompat.getColor(contexts, R.color.white))
                tvBillingType.setTextColor(ContextCompat.getColor(contexts, R.color.white))
            } else {
                cvPremiumUpgrade.setCardBackgroundColor(ContextCompat.getColor(contexts, R.color.white))
                tvSubscriptionDuration.setTextColor(ContextCompat.getColor(contexts, R.color.colorPrimary))
                tvSubscriptionDurationType.setTextColor(ContextCompat.getColor(contexts, R.color.colorPrimary))
                tvSubscriptionPrice.setTextColor(ContextCompat.getColor(contexts, R.color.black))
                tvBillingType.setTextColor(ContextCompat.getColor(contexts, R.color.black))
            }

            tvSubscriptionDuration.textOrNull = itemList[position].subscriptionDuration
            tvSubscriptionDurationType.textOrNull = itemList[position].subscriptionDurationType
            tvSubscriptionPrice.textOrNull = itemList[position].subscriptionCost
            tvBillingType.textOrNull = itemList[position].subscriptionBillDesc
        }

        holder.itemView.setOnClickListener {
            updateSelectedItem(position)
            listener.onItemClicked(itemList[position])
        }
    }

    inner class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvPremiumUpgrade: MaterialCardView = itemView.findViewById(R.id.cvPremiumUpgrade)
        val tvSubscriptionDuration: TextView = itemView.findViewById(R.id.tvSubscriptionDuration)
        val tvSubscriptionDurationType: TextView = itemView.findViewById(R.id.tvSubscriptionDurationType)
        val tvSubscriptionPrice: TextView = itemView.findViewById(R.id.tvSubscriptionPrice)
        val tvBillingType: TextView = itemView.findViewById(R.id.tvBillingType)

    }
}