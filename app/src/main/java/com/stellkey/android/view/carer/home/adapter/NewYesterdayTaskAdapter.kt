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
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SwipeToActionCallback


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version NewYesterdayTaskAdapter, v 0.1 05/02/23 14.00 by Nicolas Manurung
 */
class NewYesterdayTaskAdapter(
    var yesterdayWithChildTaskList: ArrayList<Pair<AssignmentsModel, List<AssignmentsModel>>>,
    private val listener: Listener
) : Adapter<NewYesterdayTaskAdapter.NewYesterdayTaskViewHolder>() {

    interface Listener {
        fun onYesterdayTaskItemApproved(data: AssignmentsModel)
        fun onYesterdayTaskItemDeclined(data: AssignmentsModel)
    }


    inner class NewYesterdayTaskViewHolder(private val binding: ItemTodayTaskBinding) :
        ViewHolder(binding.root) {
        fun bind(
            assignmentItem: AssignmentsModel,
            childAssignmentItem: List<AssignmentsModel>
        ) {

            val localLanguage = AppPreference.getCarerLocale() == "en"
            binding.apply {
                ivTodayChallenge.loadImage(
                    assignmentItem.icon,
                    ImageCornerOptions.ROUNDED,
                    24
                )

                tvChallengeName.textOrNull = if(localLanguage)assignmentItem.title else assignmentItem.titleFr

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
                                listener.onYesterdayTaskItemApproved(childAssignmentItem[viewHolder.absoluteAdapterPosition])
                            else
                                listener.onYesterdayTaskItemDeclined(childAssignmentItem[viewHolder.absoluteAdapterPosition])
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewYesterdayTaskViewHolder =
        NewYesterdayTaskViewHolder(
            ItemTodayTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = yesterdayWithChildTaskList.size

    override fun onBindViewHolder(holder: NewYesterdayTaskViewHolder, position: Int) {
        holder.bind(
            yesterdayWithChildTaskList[position].first,
            yesterdayWithChildTaskList[position].second.filter { it.confirmedAt == null && it.completedAt != null }
        )
    }
}