package com.stellkey.android.view.child.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel

class KidTodayTaskAdapter(
    context: Context,
    todayTaskList: ArrayList<AssignmentsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<KidTodayTaskAdapter.KidTodayTaskAdapterViewHolder>() {

    private val contexts = context
    private val itemList = todayTaskList

    interface Listener {
        fun onTodayTaskItemClicked(data: AssignmentsModel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KidTodayTaskAdapterViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_kid_today_task, parent, false)
        return KidTodayTaskAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: KidTodayTaskAdapterViewHolder, position: Int) {
        holder.apply {
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
            }
        }
    }

    inner class KidTodayTaskAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTodayTask: ImageView = itemView.findViewById(R.id.ivTodayTask)
        val tvTodayTaskName: TextView = itemView.findViewById(R.id.tvTodayTaskName)
        val ivDoneBtn: ImageView = itemView.findViewById(R.id.ivDoneBtn)
        val viewWhiteOverlay: View = itemView.findViewById(R.id.viewWhiteOverlay)
        val ivWaitingConfirmation: ImageView = itemView.findViewById(R.id.ivWaitingConfirmation)
    }
}