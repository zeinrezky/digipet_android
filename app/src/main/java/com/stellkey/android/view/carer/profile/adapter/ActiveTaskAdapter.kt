package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.task.adapter.TaskStarAdapter

class ActiveTaskAdapter(
    context: Context,
    activeTaskList: ArrayList<Pair<AssignmentsModel, List<TaskStarModel>>>,
) : RecyclerView.Adapter<ActiveTaskAdapter.ActiveTaskViewHolder>() {

    private val contexts = context
    private val itemList = activeTaskList

    private lateinit var taskStarAdapter: TaskStarAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveTaskViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_today_task, parent, false)
        return ActiveTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun selectTask(position: Int): AssignmentsModel {
        return itemList[position].first
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ActiveTaskViewHolder, position: Int) {
        val localLanguage = AppPreference.getCarerLocale() == "en"
        holder.apply {
            ivTodayChallenge.loadImage(
                itemList[position].first.icon,
                ImageCornerOptions.ROUNDED,
                24
            )
            tvChallengeName.textOrNull =
                if (localLanguage) itemList[position].first.title else itemList[position].first.titleFr

            taskStarAdapter =
                TaskStarAdapter(itemView.context, itemList[position].second.toArrayList())
            rvAssignmentStar.apply {
                layoutManager = LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = taskStarAdapter
            }
        }
    }

    inner class ActiveTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivTodayChallenge: ImageView = itemView.findViewById(R.id.ivTodayChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        val rvAssignmentStar: RecyclerView = itemView.findViewById(R.id.rvAssignmentStar)
    }
}