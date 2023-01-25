package com.stellkey.android.view.carer.family.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.util.AppPreference

class CarerListAdapter(
    context: Context,
    list: ArrayList<AllCarersModel>
) : RecyclerView.Adapter<CarerListAdapter.CarerListViewHolder>() {

    private val contexts = context
    private val itemList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarerListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_family, parent, false)
        return CarerListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun selectCarer(position: Int) {
        AppPreference.putSelectedCarerId(itemList[position].id)
        AppPreference.putTempCarerName(itemList[position].name)
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CarerListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            ivFamilyType.setImageResource(R.drawable.ic_oval_orange)
            tvFamilyName.textOrNull = itemList[position].name
            clFamilyAttr.isVisible = false
            ivNext.isGone = true
        }
    }

    inner class CarerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val ivFamilyType: ImageView = itemView.findViewById(R.id.ivFamilyType)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)
        val clFamilyAttr: ConstraintLayout = itemView.findViewById(R.id.clProfileAttr)
        val ivNext: ImageView = itemView.findViewById(R.id.ivNext)
    }
}