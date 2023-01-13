package com.stellkey.android.view.child.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidProgressStarBinding
import com.stellkey.android.model.TaskStarModel

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version MyProgressStarAdapter, v 0.1 13/01/23 19.55 by Nicolas Manurung
 */
class MyProgressStarAdapter(
    var taskStarList: List<TaskStarModel>
) : Adapter<MyProgressStarAdapter.MyProgressStarViewHolder>() {

    inner class MyProgressStarViewHolder(private val binding: ItemKidProgressStarBinding) :
        ViewHolder(binding.root) {

        fun bind(star: TaskStarModel) {
            if (star.isCompleted) {
                binding.ivProgressStar.setImageResource(R.drawable.ic_child_star_filled)
            } else {
                binding.ivProgressStar.setImageResource(R.drawable.ic_child_star)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProgressStarViewHolder =
        MyProgressStarViewHolder(
            ItemKidProgressStarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = taskStarList.size

    override fun onBindViewHolder(holder: MyProgressStarViewHolder, position: Int) {
        holder.bind(taskStarList[position])
    }
}