package com.stellkey.android.view.child.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.databinding.ItemKidProgressBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.TaskStarModel

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version MyProgressAdapter, v 0.1 13/01/23 19.45 by Nicolas Manurung
 */
class MyProgressAdapter(
    var activeTaskList: ArrayList<Pair<AssignmentsModel, List<TaskStarModel>>>,
) : Adapter<MyProgressAdapter.MyProgressViewHolder>() {

    private lateinit var myProgressStarAdapter: MyProgressStarAdapter

    inner class MyProgressViewHolder(private val binding: ItemKidProgressBinding) :
        ViewHolder(binding.root) {

        fun bind(assignment: AssignmentsModel, listProgressStar: List<TaskStarModel>) {
            binding.tvTaskName.text = assignment.title
            binding.ivTask.loadImage(assignment.icon)
            myProgressStarAdapter = MyProgressStarAdapter(listProgressStar)
            binding.rvProgressStarPart.apply {
                layoutManager = GridLayoutManager(binding.root.context, 4)
                adapter = myProgressStarAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProgressViewHolder =
        MyProgressViewHolder(
            ItemKidProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = activeTaskList.size

    override fun onBindViewHolder(holder: MyProgressViewHolder, position: Int) {
        holder.bind(activeTaskList[position].first, activeTaskList[position].second)
    }
}