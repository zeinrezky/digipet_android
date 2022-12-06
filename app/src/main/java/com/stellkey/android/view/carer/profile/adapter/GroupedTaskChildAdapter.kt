package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.TaskModel

class GroupedTaskChildAdapter(
    context: Context,
    taskList: ArrayList<TaskModel>,
    private val listener: Listener
) : RecyclerView.Adapter<GroupedTaskChildAdapter.TaskViewHolder>() {

    private val contexts = context
    private val itemList = taskList

    interface Listener {
        fun onTaskItemClicked(data: TaskModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_grouped_task_child, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.apply {
            ivChallenge.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 20)
            tvChallengeName.textOrNull = itemList[position].title
        }

        holder.itemView.setOnClickListener {
            listener.onTaskItemClicked(itemList[position])
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivChallenge: ImageView = itemView.findViewById(R.id.ivChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
    }
}