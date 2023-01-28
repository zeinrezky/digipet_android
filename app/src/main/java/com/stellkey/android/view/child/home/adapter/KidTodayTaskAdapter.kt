package com.stellkey.android.view.child.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidTodayTaskBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel

class KidTodayTaskAdapter(
    todayTaskList: ArrayList<AssignmentsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<KidTodayTaskAdapter.KidTodayTaskViewHolder>() {

    private val itemList = todayTaskList

    interface Listener {
        fun onTodayTaskItemClicked(data: AssignmentsModel)

        fun onTodayTaskItemReminderClicked(data: AssignmentsModel)
    }

    inner class KidTodayTaskViewHolder(private val binding: ItemKidTodayTaskBinding) :
        ViewHolder(binding.root) {
        fun bind(item: AssignmentsModel) {
            binding.apply {
                ivTodayTask.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 24)
                tvTodayTaskName.textOrNull = itemList[position].title
                if (itemList[position].completedAt == null) {
                    ivDoneBtn.apply {
                        isVisible = true
                        setOnClickListener { listener.onTodayTaskItemClicked(itemList[position]) }
                    }
                    viewWhiteOverlay.isVisible = false
                    ivWaitingConfirmation.isVisible = false
                } else {
                    ivDoneBtn.isVisible = false
                    viewWhiteOverlay.isVisible = true
                    ivWaitingConfirmation.isVisible = true
                    itemView.setOnClickListener {
                        listener.onTodayTaskItemReminderClicked(itemList[position])
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KidTodayTaskViewHolder = KidTodayTaskViewHolder(
        ItemKidTodayTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: KidTodayTaskViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}