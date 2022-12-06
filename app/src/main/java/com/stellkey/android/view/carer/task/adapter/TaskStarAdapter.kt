package com.stellkey.android.view.carer.task.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.model.TaskStarModel

class TaskStarAdapter(
    context: Context,
    taskStarList: ArrayList<TaskStarModel>
) : RecyclerView.Adapter<TaskStarAdapter.TaskStarViewHolder>() {

    private val contexts = context
    private val itemList = taskStarList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskStarViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_assignment_star, parent, false)
        return TaskStarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskStarViewHolder, position: Int) {
        holder.apply {
            if (itemList[position].isCompleted)
                ivAssignmentStar.setImageResource(R.drawable.ic_star_filled)
            else
                ivAssignmentStar.setImageResource(R.drawable.ic_star_outline)
        }
    }

    inner class TaskStarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAssignmentStar: ImageView = itemView.findViewById(R.id.ivAssignmentStar)
    }
}