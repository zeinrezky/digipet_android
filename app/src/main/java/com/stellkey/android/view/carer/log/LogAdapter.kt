package com.stellkey.android.view.carer.log

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.CarerLogModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant

class LogAdapter(
    context: Context,
    carerLogList: ArrayList<CarerLogModel>
) : RecyclerView.Adapter<LogAdapter.CarerLogViewHolder>() {

    private val contexts = context
    private val itemList = carerLogList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarerLogViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_carer_log, parent, false)
        return CarerLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CarerLogViewHolder, position: Int) {
        holder.apply {
            when (itemList[position].description) {
                Constant.LogType.CARER_CREATE_ASSIGNMENT -> {
                    tvName.textOrNull =
                        if (AppPreference.getLoggedInCarerName() == itemList[position].carer.name) "You" else itemList[position].carer.name
                    tvCarerLog.textOrNull = itemList[position].description
                }
                Constant.LogType.CARER_CONFIRM_ASSIGNMENT -> {
                    tvName.textOrNull = if (AppPreference.getLoggedInCarerName() == itemList[position].carer.name) "You" else itemList[position].carer.name
                    tvCarerLog.textOrNull = itemList[position].description
                }
                Constant.LogType.KID_COMPLETED_TASK -> {
                    tvName.textOrNull = itemList[position].kid.name
                    tvCarerLog.textOrNull = itemList[position].description
                }
                else -> {}
            }
        }
    }

    inner class CarerLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCarerLog: TextView = itemView.findViewById(R.id.tvCarerLog)
    }
}