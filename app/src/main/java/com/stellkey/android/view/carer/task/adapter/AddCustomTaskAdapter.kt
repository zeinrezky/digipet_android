package com.stellkey.android.view.carer.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.databinding.ItemAddCustomTaskBinding
import com.stellkey.android.model.CategorizedTaskModel

class AddCustomTaskAdapter(
    val categoryTaskModel: CategorizedTaskModel,
    private val listener: Listener
) : RecyclerView.Adapter<AddCustomTaskAdapter.AddTaskViewHolder>() {

    interface Listener {

        fun onAddCustomTaskClicked(categoryTaskModel: CategorizedTaskModel)
    }

    inner class AddTaskViewHolder(private val binding: ItemAddCustomTaskBinding) :
        ViewHolder(binding.root) {

        fun bind(item: String) {
            itemView.setOnClickListener {
                listener.onAddCustomTaskClicked(categoryTaskModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTaskViewHolder =
        AddTaskViewHolder(
            ItemAddCustomTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: AddTaskViewHolder, position: Int) {
        holder.bind("custom-task")
    }
}