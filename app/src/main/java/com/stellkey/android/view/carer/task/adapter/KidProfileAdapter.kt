package com.stellkey.android.view.carer.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidProfileBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllKidsModel
import kotlinx.android.synthetic.main.item_kid_profile.view.cvKidAvatar

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version KidProfileAdapter, v 0.1 07/01/23 14.26 by Nicolas Manurung
 */
class KidProfileAdapter(
    var kidProfileList: ArrayList<AllKidsModel> = arrayListOf(),
    val listener: Listener? = null
) : Adapter<KidProfileAdapter.KidProfileViewHolder>() {

    interface Listener {

        fun onKidProfileSelected(data: AllKidsModel)
    }

    inner class KidProfileViewHolder(private val binding: ItemKidProfileBinding) :
        ViewHolder(binding.root) {

        fun bind(data: AllKidsModel) {
            if (data.uiAction.isSelected) {
                itemView.cvKidAvatar.strokeWidth = 10
                itemView.cvKidAvatar.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            } else {
                itemView.cvKidAvatar.strokeWidth = 10
                itemView.cvKidAvatar.strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.light_grey)

            }
            binding.ivKidAvatar.loadImage(
                data.profileIcon.icon,
                ImageCornerOptions.ROUNDED,
                100
            )
            binding.tvKidName.textOrNull = data.name
            itemView.setOnClickListener {
                listener?.onKidProfileSelected(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidProfileViewHolder =
        KidProfileViewHolder(
            ItemKidProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = kidProfileList.size

    override fun onBindViewHolder(holder: KidProfileViewHolder, position: Int) {
        holder.bind(kidProfileList[position])
    }

    fun setProfileListInto(listKid: ArrayList<AllKidsModel>) {
        this.kidProfileList.clear()
        kidProfileList.addAll(listKid)
        notifyDataSetChanged()
    }
}