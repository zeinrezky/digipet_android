package com.stellkey.android.view.carer.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemGroupedTaskBinding
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.CategorizedTaskModel
import com.stellkey.android.model.TaskModel

class GroupedTaskAdapter(
    context: Context,
    taskCategoryList: ArrayList<CategorizedTaskModel>,
    private val listener: Listener
) : RecyclerView.Adapter<GroupedTaskAdapter.GroupedTaskViewHolder>(),
    GroupedTaskChildAdapter.Listener {

    private val contexts = context
    private val itemList = taskCategoryList
    private var isExpandChildList = emptyBoolean

    private lateinit var groupedTaskChildAdapter: GroupedTaskChildAdapter

    interface Listener {

        fun onGroupedTaskItemClicked(data: TaskModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupedTaskViewHolder =
        GroupedTaskViewHolder(
            ItemGroupedTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: GroupedTaskViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class GroupedTaskViewHolder(private val binding: ItemGroupedTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategorizedTaskModel) {
            val addedCatTaskList = item.tasks

            addedCatTaskList.forEach {
                it.challengeCatId = item.id
            }

            binding.apply {
                tvCategoryName.textOrNull = item.title

                ivExpand.apply {
                    isVisible = true
                    setOnClickListener {
                        if (isExpandChildList) {
                            setImageResource(R.drawable.ic_arrow_down)
                            isExpandChildList = false
                            rvGroupedTaskChild.isVisible = false
                        } else {
                            setImageResource(R.drawable.ic_arrow_up)
                            isExpandChildList = true
                            groupedTaskChildAdapter = GroupedTaskChildAdapter(
                                contexts,
                                addedCatTaskList,
                                this@GroupedTaskAdapter
                            )
                            rvGroupedTaskChild.apply {
                                isVisible = true
                                layoutManager = LinearLayoutManager(
                                    contexts,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = groupedTaskChildAdapter
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onTaskItemClicked(data: TaskModel) {
        listener.onGroupedTaskItemClicked(data)
    }
}