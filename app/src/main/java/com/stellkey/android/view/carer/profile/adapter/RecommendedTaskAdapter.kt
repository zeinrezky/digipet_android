package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.RecommendedTaskModel

class RecommendedTaskAdapter(
    context: Context,
    list: ArrayList<RecommendedTaskModel>,
    private val listener: Listener
) : RecyclerView.Adapter<RecommendedTaskAdapter.TaskListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: RecommendedTaskModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_recommended_task, parent, false)
        return TaskListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.apply {
            cvKidChallenge.setOnClickListener {
                listener.onItemClicked(itemList[position])
            }
            ivChallenge.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 100)
            tvChallengeName.textOrNull = itemList[position].title
        }
    }

    inner class TaskListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvKidChallenge: MaterialCardView = itemView.findViewById(R.id.cvKidChallenge)
        val ivChallenge: ImageView = itemView.findViewById(R.id.ivChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
    }
}