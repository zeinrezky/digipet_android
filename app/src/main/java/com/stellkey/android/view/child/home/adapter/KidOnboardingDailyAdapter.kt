package com.stellkey.android.view.child.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.databinding.ItemOnboardingDailyTaskBinding
import com.stellkey.android.helper.extension.loadFromUrl
import com.stellkey.android.model.AssignmentsModel


class KidOnboardingDailyAdapter(
    var listDailyTask: List<AssignmentsModel>,
    val onItemClick: (AssignmentsModel) -> Unit = {},
) :
    Adapter<KidOnboardingDailyAdapter.KidOnboardingDailyViewHolder>() {

    inner class KidOnboardingDailyViewHolder(private val binding: ItemOnboardingDailyTaskBinding) :
        ViewHolder(binding.root) {
        fun bind(item: AssignmentsModel) {
            binding.apply {
                ivTask.loadFromUrl(item.icon)
                tvTodayTaskName.text = item.title
                ivDoneBtn.setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KidOnboardingDailyViewHolder = KidOnboardingDailyViewHolder(
        ItemOnboardingDailyTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = listDailyTask.size

    override fun onBindViewHolder(holder: KidOnboardingDailyViewHolder, position: Int) {
        holder.bind(listDailyTask[position])
    }
}