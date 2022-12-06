package com.stellkey.android.view.carer.family.adapter

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
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.FamilyTypeModel
import com.stellkey.android.util.Constant

class FamilyTypeAdapter(
    context: Context,
    list: ArrayList<FamilyTypeModel>,
    private val listener: Listener
) : RecyclerView.Adapter<FamilyTypeAdapter.FamilyTypeViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: FamilyTypeModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyTypeViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_family_type, parent, false)
        return FamilyTypeViewHolder(view)
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

    override fun onBindViewHolder(holder: FamilyTypeViewHolder, position: Int) {
        holder.apply {
            if (itemList[position].isSelected) {
                cvFamilyType.setCardBackgroundColor(
                    ContextCompat.getColor(
                        contexts,
                        R.color.colorPrimary
                    )
                )
                tvFamilyType.setTextColor(ContextCompat.getColor(contexts, R.color.white))
            } else {
                cvFamilyType.setCardBackgroundColor(ContextCompat.getColor(contexts, R.color.white))
                tvFamilyType.setTextColor(ContextCompat.getColor(contexts, R.color.colorPrimary))
            }

            when (itemList[position].type) {
                Constant.FamilyType.KIDS -> {
                    ivFamilyType.setImageResource(R.drawable.ic_oval_yellow)
                }
                Constant.FamilyType.CARERS -> {
                    ivFamilyType.setImageResource(R.drawable.ic_oval_orange)
                }
                Constant.FamilyType.ADMIN -> {
                    ivFamilyType.setImageResource(R.drawable.ic_oval_grey)
                }
                else -> {}
            }
            tvFamilyType.textOrNull = itemList[position].type
        }

        holder.itemView.setOnClickListener {
            updateSelectedItem(position)
            listener.onItemClicked(itemList[position])
        }
    }

    inner class FamilyTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvFamilyType: MaterialCardView = itemView.findViewById(R.id.cvFamilyType)
        val ivFamilyType: ImageView = itemView.findViewById(R.id.ivFamilyType)
        val tvFamilyType: TextView = itemView.findViewById(R.id.tvFamilyType)

    }
}