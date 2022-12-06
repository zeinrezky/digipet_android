package com.stellkey.android.view.carer.family.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllCarersModel

class AdminListAdapter(
    context: Context,
    list: ArrayList<AllCarersModel>,
    private val listener: Listener
) : RecyclerView.Adapter<AdminListAdapter.AdminListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onAdminItemClicked(data: AllCarersModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_family, parent, false)
        return AdminListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AdminListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            ivFamilyType.setImageResource(R.drawable.ic_oval_grey)
            tvFamilyName.textOrNull = itemList[position].name
            clFamilyAttr.isVisible = false
        }

        holder.itemView.setOnClickListener {
            listener.onAdminItemClicked(itemList[position])
        }
    }

    inner class AdminListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val ivFamilyType: ImageView = itemView.findViewById(R.id.ivFamilyType)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)
        val clFamilyAttr: ConstraintLayout = itemView.findViewById(R.id.clProfileAttr)
    }
}