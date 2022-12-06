package com.stellkey.android.view.carer.family.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.util.AppPreference

class KidListAdapter(
    context: Context,
    list: ArrayList<AllKidsModel>,
    private val listener: Listener
) : RecyclerView.Adapter<KidListAdapter.KidListViewHolder>() {

    private val contexts = context
    private val itemList = list

    interface Listener {
        fun onItemClicked(data: AllKidsModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidListViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_family, parent, false)
        return KidListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun selectKid(position: Int) {
        AppPreference.putTempChildId(itemList[position].id)
        AppPreference.putTempChildName(itemList[position].name)
    }

    fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: KidListViewHolder, position: Int) {
        holder.apply {
            ivAvatar.loadImage(itemList[position].profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
            ivFamilyType.setImageResource(R.drawable.ic_oval_yellow)
            tvFamilyName.textOrNull = itemList[position].name
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(itemList[position])
        }
    }

    inner class KidListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val ivFamilyType: ImageView = itemView.findViewById(R.id.ivFamilyType)
        val tvFamilyName: TextView = itemView.findViewById(R.id.tvFamilyName)

    }
}