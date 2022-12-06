package com.stellkey.android.view.carer.home.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TodayTaskChildAdapter(
    context: Context,
    activeAssignmentList: ArrayList<AssignmentsModel>
) : RecyclerView.Adapter<TodayTaskChildAdapter.TodayTaskChildViewHolder>() {

    private val contexts = context
    private val itemList = activeAssignmentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskChildViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_today_task_child, parent, false)
        return TodayTaskChildViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun clearList() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TodayTaskChildViewHolder, position: Int) {
        holder.apply {
            val current = itemList[position].assignDate
            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val formatted = parser.parse(current)?.let { formatter.format(it) }

            tvAssignDate.textOrNull = formatted
        }
    }

    inner class TodayTaskChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAssignDate: TextView = itemView.findViewById(R.id.tvAssignDate)
    }
}