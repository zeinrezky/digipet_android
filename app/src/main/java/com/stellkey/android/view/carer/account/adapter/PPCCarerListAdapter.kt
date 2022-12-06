package com.stellkey.android.view.carer.account.adapter

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
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.util.AppPreference

class PPCCarerListAdapter(
    context: Context,
    list: ArrayList<AllCarersModel>,
    private val listener: Listener
) : RecyclerView.Adapter<PPCCarerListAdapter.CarerListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onCarerEditProfileClicked(data: AllCarersModel)
        fun onCarerEditPINClicked(data: AllCarersModel)
        fun onCarerItemDeleteClicked(data: AllCarersModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarerListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_ppc_carer, parent, false)
        return CarerListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun selectCarerId(position: Int) {
        AppPreference.putSelectedCarerId(itemList[position].id)
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CarerListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            tvFamilyName.textOrNull = itemList[position].name

            tvEditProfile.setOnClickListener { listener.onCarerEditProfileClicked(itemList[position]) }
            tvEditPIN.setOnClickListener { listener.onCarerEditPINClicked(itemList[position]) }
            tvRemoveProfile.setOnClickListener { listener.onCarerItemDeleteClicked(itemList[position]) }
        }
    }

    inner class CarerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)
        val tvEditProfile: TextView = itemView.findViewById(R.id.tvEditProfile)
        val tvEditPIN: TextView = itemView.findViewById(R.id.tvEditPIN)
        val tvRemoveProfile: TextView = itemView.findViewById(R.id.tvRemoveProfile)
    }
}