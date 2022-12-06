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
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.util.SwipeToActionCallback
import com.stellkey.android.view.carer.home.HomeAct

class TodayTaskAdapter(
    context: Context,
    todayTaskList: ArrayList<AssignmentsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<TodayTaskAdapter.TodayTaskViewHolder>() {

    private val contexts = context
    private val itemList = todayTaskList
    private var notificationCount = 0
    private var notificationTotal = 0
    private var isExpandChildList = emptyBoolean

    private lateinit var todayTaskChildAdapter: TodayTaskChildAdapter
    var todayTaskChildList = arrayListOf<AssignmentsModel>()

    interface Listener {
        fun onTodayTaskItemClicked(data: AssignmentsModel)
        fun onTodayTaskItemApproved(data: AssignmentsModel)
        fun onTodayTaskItemDeclined(data: AssignmentsModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_today_task, parent, false)
        return TodayTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun removeNotification() {
        notificationCount = 0
        notificationTotal = 0

        todayTaskChildList.clear()
        todayTaskChildAdapter.clearList()
        notifyDataSetChanged()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodayTaskViewHolder, position: Int) {
        holder.apply {
            ivTodayChallenge.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 24)
            tvChallengeName.textOrNull = itemList[position].title

            /*val sortedChildItemList = arrayListOf<AssignmentsModel>()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            val dateNow = LocalDateTime.now().format(formatter)
            val dateNowFormatted = sdf.parse(dateNow)

            itemList.forEach {
                val itemDate = sdf.parse(it.assignDate)
                val cmp = itemDate?.compareTo(dateNowFormatted).orEmpty

                if (cmp == 0) {
                    if (it.completedAt.isEmpty()) {
                        notificationCount += 1
                        sortedChildItemList.add(it)
                    }
                }
            }*/

            if (itemList[position].completedAt != null) {
                if (itemList[position].confirmedAt != null)
                    notificationCount = 0
                else if (itemList[position].declinedAt != null)
                    notificationCount = 0
                else {
                    notificationCount += 1
                    notificationTotal += notificationCount
                }
            } else
                notificationCount = 0

            (contexts as HomeAct).setBadge(notificationTotal)

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

                            todayTaskChildList.apply {
                                clear()
                                add(itemList[position])
                            }

                            todayTaskChildAdapter = TodayTaskChildAdapter(
                                contexts,
                                todayTaskChildList
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
                        if (direction == ItemTouchHelper.RIGHT)
                            listener.onTodayTaskItemApproved(itemList[adapterPosition])
                        else
                            listener.onTodayTaskItemDeclined(itemList[adapterPosition])
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

    inner class TodayTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTodayChallenge: ImageView = itemView.findViewById(R.id.ivTodayChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        val ivNotificationCount: ImageView = itemView.findViewById(R.id.ivNotificationCount)
        val tvNotificationCount: TextView = itemView.findViewById(R.id.tvNotificationCount)
        val ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        val clTodayTaskChild: ConstraintLayout = itemView.findViewById(R.id.clTodayTaskChild)
        val rvTodayTaskChild: RecyclerView = itemView.findViewById(R.id.rvTodayTaskChild)
    }
}