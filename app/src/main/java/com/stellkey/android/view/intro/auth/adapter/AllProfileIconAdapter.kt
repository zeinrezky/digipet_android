package com.stellkey.android.view.intro.auth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.ProfileIconModel

class AllProfileIconAdapter(
    context: Context,
    list: ArrayList<ProfileIconModel.ProfileIconModelData>,
    private val listener: Listener
) : RecyclerView.Adapter<AllProfileIconAdapter.ProfileIconViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: ProfileIconModel.ProfileIconModelData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileIconViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_all_profile_icons, parent, false)
        return ProfileIconViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ProfileIconViewHolder, position: Int) {
        holder.ivProfileIcon.loadImage(itemList[position].icon, ImageCornerOptions.ROUNDED, 100)

        holder.itemView.setOnClickListener {
            listener.onItemClicked(itemList[position])
        }
    }

    inner class ProfileIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileIcon: ImageView = itemView.findViewById(R.id.ivProfileIcon)

    }
}