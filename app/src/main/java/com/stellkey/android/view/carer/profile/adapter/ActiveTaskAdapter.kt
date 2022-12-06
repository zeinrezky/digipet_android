package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.util.AppPreference

class ActiveTaskAdapter(
    context: Context,
    activeTaskList: ArrayList<AssignmentsModel>
) : RecyclerView.Adapter<ActiveTaskAdapter.ActiveTaskViewHolder>() {

    private val contexts = context
    private val itemList = activeTaskList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveTaskViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_today_task, parent, false)
        return ActiveTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun selectTask(position: Int) : AssignmentsModel{
        return itemList[position]
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ActiveTaskViewHolder, position: Int) {
        holder.apply {
            ivTodayChallenge.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 24)
            tvChallengeName.textOrNull = itemList[position].title
        }
    }

    inner class ActiveTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTodayChallenge: ImageView = itemView.findViewById(R.id.ivTodayChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
    }
}