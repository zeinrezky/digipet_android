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
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.util.AppPreference

class PPCKidListAdapter(
    context: Context,
    list: ArrayList<AllKidsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<PPCKidListAdapter.KidListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onKidEditProfileClicked(data: AllKidsModel)
        fun onKidEditPINClicked(data: AllKidsModel)
        fun onKidItemDeleteClicked(data: AllKidsModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_ppc_kid, parent, false)
        return KidListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: KidListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            tvFamilyName.textOrNull = itemList[position].name

            tvEditProfile.setOnClickListener {
                listener.onKidEditProfileClicked(itemList[position])
            }
            tvEditPIN.setOnClickListener {
                listener.onKidEditPINClicked(itemList[position])
            }
            tvRemoveProfile.setOnClickListener {
                listener.onKidItemDeleteClicked(itemList[position])
            }
        }
    }

    inner class KidListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)
        val tvEditProfile: TextView = itemView.findViewById(R.id.tvEditProfile)
        val tvEditPIN: TextView = itemView.findViewById(R.id.tvEditPIN)
        val tvRemoveProfile: TextView = itemView.findViewById(R.id.tvRemoveProfile)
    }
}