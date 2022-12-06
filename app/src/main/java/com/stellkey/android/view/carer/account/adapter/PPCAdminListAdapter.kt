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

class PPCAdminListAdapter(
    context: Context,
    list: ArrayList<AllCarersModel>,
    private val listener: Listener
) : RecyclerView.Adapter<PPCAdminListAdapter.AdminListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onAdminEditProfileClicked(data: AllCarersModel)
        fun onAdminEditPasswordClicked(data: AllCarersModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_ppc_admin, parent, false)
        return AdminListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AdminListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            tvFamilyName.textOrNull = itemList[position].name

            tvEditProfile.setOnClickListener {
                listener.onAdminEditProfileClicked(itemList[position])
            }
            tvEditPassword.setOnClickListener {
                listener.onAdminEditPasswordClicked(itemList[position])
            }
        }
    }

    inner class AdminListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)
        val tvEditProfile: TextView = itemView.findViewById(R.id.tvEditProfile)
        val tvEditPassword: TextView = itemView.findViewById(R.id.tvEditPassword)
    }
}