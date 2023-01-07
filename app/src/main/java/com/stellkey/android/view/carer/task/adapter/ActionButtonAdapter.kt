package com.stellkey.android.view.carer.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidProfileBtnBinding

class ActionButtonAdapter(
    var type: ButtonActionType,
    val listener: Listener
) : Adapter<ActionButtonAdapter.ActionButtonViewHolder>() {

    interface Listener {

        fun onActionButtonClicked(type: ButtonActionType)
    }

    inner class ActionButtonViewHolder(private val binding: ItemKidProfileBtnBinding) :
        ViewHolder(binding.root) {

        fun bind(empty: String) {
            when (type) {
                ButtonActionType.ADD_ICON -> {
                    binding.ivBtnKid.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_plus
                        )
                    )
                }

                ButtonActionType.DONE_ICON -> {
                    binding.ivBtnKid.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_done
                        )
                    )
                }
            }

            itemView.setOnClickListener {
                listener.onActionButtonClicked(type)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionButtonViewHolder =
        ActionButtonViewHolder(
            ItemKidProfileBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ActionButtonViewHolder, position: Int) {
        holder.bind("")
    }

    fun setIconType(typeIcon: ButtonActionType) {
        this.type = typeIcon
        notifyDataSetChanged()
    }

    enum class ButtonActionType {
        ADD_ICON,
        DONE_ICON
    }
}