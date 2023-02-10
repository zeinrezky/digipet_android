package com.stellkey.android.view.carer.home.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SwipeToActionCallback
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*

class YesterdayTaskAdapter(
    context: Context,
    yesterdayTaskList: ArrayList<AssignmentsModel>,
    activeAssignmentList: ArrayList<AssignmentsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<YesterdayTaskAdapter.YesterdayTaskViewHolder>() {

    private val contexts = context
    private val itemList = yesterdayTaskList
    private val childItemList = activeAssignmentList
    private var notificationCount = 0
    private var isExpandChildList = emptyBoolean

    private lateinit var todayTaskChildAdapter: TodayTaskChildAdapter

    private var selectedChildPosition = emptyInt

    interface Listener {
        fun onYesterdayTaskItemClicked(data: AssignmentsModel)
        fun onYesterdayTaskItemApproved(data: AssignmentsModel)
        fun onYesterdayTaskItemDeclined(data: AssignmentsModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YesterdayTaskViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_today_task, parent, false)
        return YesterdayTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun removeNotification() {
        notificationCount -= 1
        todayTaskChildAdapter.removeAt(selectedChildPosition)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: YesterdayTaskViewHolder, position: Int) {
        val localLanguage = AppPreference.getCarerLocale() == "en"

        holder.apply {
            notificationCount = 0
            ivTodayChallenge.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 24)
            tvChallengeName.textOrNull =
                if (localLanguage) itemList[position].title else itemList[position].titleFr

            val sortedChildItemList = arrayListOf<AssignmentsModel>()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            val dateNow = LocalDateTime.now().minusDays(1).format(formatter)
            val dateNowFormatted = sdf.parse(dateNow)
            val minutesOfDayNow = LocalDateTime.now().get(ChronoField.MINUTE_OF_DAY)

            println("Date -1: $minutesOfDayNow")
            println(
                "Item Date: ${
                    sdf.parse(itemList[position].assignDate)
                }"
            )

            childItemList.forEach {
                val itemDate = sdf.parse(it.assignDate)
                val cmp = itemDate.compareTo(dateNowFormatted)

                if (cmp < 0) {
                    if (it.completedAt.isNullOrEmpty()) {
                        /*val minutesOfDayToCompare =
                            itemDate.toInstant().get(ChronoField.MINUTE_OF_DAY)*/
                        notificationCount += 1
                        sortedChildItemList.add(it)
                        /*if (minutesOfDayNow > minutesOfDayToCompare) {
                            notificationCount += 1
                            sortedChildItemList.add(it)
                        }*/
                    }
                }

                if (cmp == 0) {
                    if (it.completedAt.isNullOrEmpty()) {
                        notificationCount += 1
                        sortedChildItemList.add(it)
                    }
                }
            }

            if (notificationCount > 0) {
                ivNotificationCount.isVisible = true
                tvNotificationCount.apply {
                    isVisible = true
                    textOrNull = notificationCount.toString()
                }
                ivExpand.apply {
                    isVisible = true
                    setOnClickListener {
                        if (isExpandChildList) {
                            setImageResource(R.drawable.ic_arrow_down)
                            isExpandChildList = false
                            clTodayTaskChild.isVisible = false
                        } else {
                            setImageResource(R.drawable.ic_arrow_up)
                            isExpandChildList = true
                            clTodayTaskChild.isVisible = true
                            todayTaskChildAdapter = TodayTaskChildAdapter(
                                contexts,
                                sortedChildItemList
                            )

                            rvTodayTaskChild.apply {
                                layoutManager = LinearLayoutManager(
                                    contexts,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                adapter = todayTaskChildAdapter
                            }
                        }
                    }
                }

                val swipeHandler = object : SwipeToActionCallback(contexts) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        selectedChildPosition = viewHolder.adapterPosition
                        if (direction == ItemTouchHelper.RIGHT)
                            listener.onYesterdayTaskItemApproved(itemList[adapterPosition])
                        else
                            listener.onYesterdayTaskItemDeclined(itemList[adapterPosition])
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(rvTodayTaskChild)

            } else {
                ivNotificationCount.isVisible = false
                tvNotificationCount.isVisible = false
                ivExpand.isVisible = false
                clTodayTaskChild.isVisible = false
            }
        }

    }

    inner class YesterdayTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTodayChallenge: ImageView = itemView.findViewById(R.id.ivTodayChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        val ivNotificationCount: ImageView = itemView.findViewById(R.id.ivNotificationCount)
        val tvNotificationCount: TextView = itemView.findViewById(R.id.tvNotificationCount)
        val ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        val clTodayTaskChild: ConstraintLayout = itemView.findViewById(R.id.clTodayTaskChild)
        val rvTodayTaskChild: RecyclerView = itemView.findViewById(R.id.rvTodayTaskChild)
    }
}