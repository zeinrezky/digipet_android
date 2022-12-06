package com.stellkey.android.view.intro.auth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllProfileModel

class AllProfileAdapter(
    context: Context,
    list: ArrayList<AllProfileModel.Family>,
    private val listener: Listener
) : RecyclerView.Adapter<AllProfileAdapter.AllProfileViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: AllProfileModel.Family)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProfileViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_all_profile, parent, false)
        return AllProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AllProfileViewHolder, position: Int) {

        holder.ivProfileIcon.loadImage(
            itemList[position].profileIcon.icon,
            ImageCornerOptions.ROUNDED,
            100
        )
        holder.tvProfileName.textOrNull = itemList[position].name

        holder.itemView.setOnClickListener {
            listener.onItemClicked(itemList[position])
        }
    }

    inner class AllProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileIcon: ImageView = itemView.findViewById(R.id.ivProfileIcon)
        val tvProfileName: TextView = itemView.findViewById(R.id.tvProfileName)
    }
}