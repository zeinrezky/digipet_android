package com.stellkey.android.view.carer.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemTodayTaskBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SwipeToActionCallback
import com.stellkey.android.view.carer.task.adapter.TaskStarAdapter


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version NewTodayTaskAdapter, v 0.1 05/02/23 11.44 by Nicolas Manurung
 */
class NewTodayTaskAdapter(
    var todayTaskList: ArrayList<Pair<AssignmentsModel, List<TaskStarModel>>>,
    var childTaskList: ArrayList<List<AssignmentsModel>>,
    private val listener: Listener
) : Adapter<NewTodayTaskAdapter.NewTodayTaskViewHolder>() {

    interface Listener {
        fun onTodayTaskItemApproved(data: AssignmentsModel)
        fun onTodayTaskItemDeclined(data: AssignmentsModel)
    }

    inner class NewTodayTaskViewHolder(private val binding: ItemTodayTaskBinding) :
        ViewHolder(binding.root) {
        fun bind(
            assignmentItem: AssignmentsModel,
            starTaskItem: List<TaskStarModel>,
            childAssignmentItem: List<AssignmentsModel>
        ) {
            val localLanguage = AppPreference.getCarerLocale() == "en"
            binding.apply {
                ivTodayChallenge.loadImage(
                    assignmentItem.icon,
                    ImageCornerOptions.ROUNDED,
                    24
                )

                tvChallengeName.textOrNull = if (localLanguage) assignmentItem.title else assignmentItem.titleFr
                rvAssignmentStar.apply {
                    layoutManager = LinearLayoutManager(
                        itemView.context, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    adapter = TaskStarAdapter(itemView.context, starTaskItem.toArrayList())
                }

                if (childAssignmentItem.isNotEmpty()) {
                    setVisibleNotificationCount(true, childAssignmentItem.size)
                    rvTodayTaskChild.apply {
                        layoutManager = LinearLayoutManager(itemView.context)
                        adapter = TodayTaskChildAdapter(
                            itemView.context,
                            childAssignmentItem.toArrayList()
                        )
                    }
                    val swipeHandler = object : SwipeToActionCallback(itemView.context) {
                        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                            if (direction == ItemTouchHelper.RIGHT)
                                listener.onTodayTaskItemApproved(childAssignmentItem[viewHolder.absoluteAdapterPosition])
                            else
                                listener.onTodayTaskItemDeclined(childAssignmentItem[viewHolder.absoluteAdapterPosition])
                        }
                    }
                    val itemTouchHelper = ItemTouchHelper(swipeHandler)
                    itemTouchHelper.attachToRecyclerView(rvTodayTaskChild)
                } else {
                    setVisibleNotificationCount(false, 0)
                }
            }
        }

        private fun setVisibleNotificationCount(isVisible: Boolean, sumNotification: Int) {
            binding.apply {
                ivNotificationCount.isVisible = isVisible
                tvNotificationCount.isVisible = isVisible
                ivExpand.isVisible = isVisible
                tvNotificationCount.textOrNull = sumNotification.toString()
                clTodayTaskChild.isVisible = false
                if (isVisible) {
                    setListenerOnTodayTaskChild()
                }
            }
        }

        private fun setListenerOnTodayTaskChild() {
            var defaultFalseExpand = false
            binding.apply {
                ivExpand.setOnClickListener {
                    if (defaultFalseExpand) {
                        defaultFalseExpand = false
                        clTodayTaskChild.isVisible = false
                        ivExpand.setImageResource(R.drawable.ic_arrow_down)
                    } else {
                        defaultFalseExpand = true
                        clTodayTaskChild.isVisible = true
                        ivExpand.setImageResource(R.drawable.ic_arrow_up)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewTodayTaskViewHolder =
        NewTodayTaskViewHolder(
            ItemTodayTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = todayTaskList.size

    override fun onBindViewHolder(holder: NewTodayTaskViewHolder, position: Int) {
        holder.bind(
            todayTaskList[position].first,
            todayTaskList[position].second,
            childTaskList[position].filter { it.confirmedAt == null && it.completedAt != null }
        )
    }
}