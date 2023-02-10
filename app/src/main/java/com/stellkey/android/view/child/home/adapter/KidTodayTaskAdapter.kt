package com.stellkey.android.view.child.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidTodayTaskBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.util.AppPreference

class KidTodayTaskAdapter(
    todayTaskList: ArrayList<AssignmentsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<KidTodayTaskAdapter.KidTodayTaskViewHolder>() {

    private val itemList = todayTaskList

    interface Listener {
        fun onTodayTaskCollect(data: AssignmentsModel)

        fun onTodayTaskReminder(data: AssignmentsModel)

        fun onTodayTaskCompleted(data: AssignmentsModel)
    }

    inner class KidTodayTaskViewHolder(private val binding: ItemKidTodayTaskBinding) :
        ViewHolder(binding.root) {
        private val localLanguage = AppPreference.getCarerLocale() == "en"
        fun bind(item: AssignmentsModel) {
            binding.apply {
                ivTodayTask.loadImage(item.icon, ImageCornerOptions.ROUNDED, 24)
                tvTodayTaskName.textOrNull = if (localLanguage) item.title else item.titleFr

                // completed
                if (item.completedAt != null && item.confirmedAt != null) {
                    clNotConfirmed.isVisible = false
                    clNotCompleted.isInvisible = true
                    clCompleted.isVisible = true
                    root.setOnClickListener {
                        listener.onTodayTaskCompleted(item)
                    }
                    return
                }

                // collect
                if (item.completedAt == null && item.confirmedAt == null) {
                    clCompleted.isVisible = false
                    clNotConfirmed.isVisible = false
                    clNotCompleted.isVisible = true
                    ivDoneBtn.setOnClickListener {
                        listener.onTodayTaskCollect(item)
                    }
                    return
                }

                // reminder
                if (item.completedAt != null) {
                    clCompleted.isVisible = false
                    clNotConfirmed.isVisible = true
                    clNotCompleted.isVisible = true
                    ivDoneBtn.isVisible = false
                    clKidTodayTask.background =
                        ContextCompat.getDrawable(root.context, R.color.transparent)
                    root.setOnClickListener {
                        listener.onTodayTaskReminder(item)
                    }
                    return
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