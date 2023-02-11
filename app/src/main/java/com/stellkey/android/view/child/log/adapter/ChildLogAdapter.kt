package com.stellkey.android.view.child.log.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.R
import com.stellkey.android.databinding.ItemKidLogBinding
import com.stellkey.android.databinding.ItemLogDescriptionBinding
import com.stellkey.android.helper.extension.append
import com.stellkey.android.model.KidLogModel
import com.stellkey.android.util.AppPreference

class ChildLogAdapter(
    var listLogs: ArrayList<Pair<String, List<KidLogModel>>> // date / list item log
) : Adapter<ChildLogAdapter.ChildLogViewHolder>() {

    inner class ChildLogViewHolder(private val binding: ItemKidLogBinding) :
        ViewHolder(binding.root) {

        fun bind(date: String, item: List<KidLogModel>) {
            binding.tvDateLog.text = date
            binding.rvItemActivity.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = ChildLogItemAdapter(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildLogViewHolder =
        ChildLogViewHolder(
            ItemKidLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = listLogs.size

    override fun onBindViewHolder(holder: ChildLogViewHolder, position: Int) {
        holder.bind(listLogs[position].first, listLogs[position].second)
    }

    inner class ChildLogItemAdapter(
        var listItemDescription: List<KidLogModel>
    ) : Adapter<ChildLogItemAdapter.ChildLogItemViewHolder>() {

        inner class ChildLogItemViewHolder(private val binding: ItemLogDescriptionBinding) :
            ViewHolder(binding.root) {
            val localeLanguage = AppPreference.getKidLocale() == "en"

            @SuppressLint("SetTextI18n")
            fun bind(item: KidLogModel, position: Int) {
                when (item.description) {
                    LOG_TYPE_CONFIRM_ASSIGNMENT -> {
                        item.challenge?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(
                                    R.string.kid_log_approved,
                                    item.carer.name
                                )
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr

                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(
                                        R.string.kid_log_approved,
                                        item.carer.name
                                    )
                                append(title, R.color.colorPrimary)
                            }
                        }
                    }

                    LOG_TYPE_DECLINE_ASSIGNMENT -> {
                        item.challenge?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(
                                    R.string.kid_log_decline,
                                    item.carer.name
                                )
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr

                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(
                                        R.string.kid_log_decline,
                                        item.carer.name
                                    )
                                append(title, R.color.colorPrimary)
                            }
                        }
                    }

                    LOG_TYPE_COMPLETED_ASSIGNMENT -> {
                        item.challenge?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(R.string.kid_log_completed)
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr

                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(R.string.kid_log_completed)
                                append(title, R.color.colorPrimary)
                            }
                        }
                    }

                    LOG_TYPE_REDEEM_REWARD -> {
                        item.reward?.star_cost?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(R.string.kid_log_collected)
                            binding.tvLogDescription.append(it.toString(), R.color.colorPrimary)
                        }
                    }

                    LOG_TYPE_CREATED_ASSIGNMENT -> {
                        item.challenge?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(
                                    R.string.kid_log_created,
                                    item.carer.name
                                )
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr
                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(
                                        R.string.kid_log_created,
                                        item.carer.name
                                    )
                                append(title, R.color.colorPrimary)
                            }
                        }
                    }

                    LOG_TYPE_CONFIRM_REDEMPTION -> {
                        item.reward?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(
                                    R.string.kid_log_approved,
                                    item.carer.name
                                )
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr

                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(
                                        R.string.kid_log_approved,
                                        item.carer.name
                                    )
                                append(title, R.color.colorPrimary)
                            }
                        }
                    }

                    LOG_TYPE_DECLINE_REDEMPTION -> {
                        item.reward?.title?.let {
                            binding.tvLogDescription.text =
                                "$position." + binding.root.context.getString(
                                    R.string.kid_log_decline,
                                    item.carer.name
                                )
                            binding.tvLogDescription.append(it, R.color.colorPrimary)
                        }

                        binding.tvLogDescription.apply {
                            val title =
                                if (localeLanguage) item.globalChallenge?.title else item.globalChallenge?.titleFr

                            if (title?.isNotEmpty() == true) {
                                text =
                                    "$position." + binding.root.context.getString(
                                        R.string.kid_log_decline,
                                        item.carer.name
                                    )
                                binding.tvLogDescription.append(title, R.color.colorPrimary)
                            }
                        }
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildLogItemViewHolder =
            ChildLogItemViewHolder(
                ItemLogDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun getItemCount(): Int = listItemDescription.size

        override fun onBindViewHolder(holder: ChildLogItemViewHolder, position: Int) {
            holder.bind(listItemDescription[position], position + 1)
        }
    }

    companion object {
        const val LOG_TYPE_CONFIRM_ASSIGNMENT = "confirm assignment"
        const val LOG_TYPE_DECLINE_ASSIGNMENT = "decline assignment"
        const val LOG_TYPE_COMPLETED_ASSIGNMENT = "completed task"
        const val LOG_TYPE_REDEEM_REWARD = "redeem reward"
        const val LOG_TYPE_CREATED_ASSIGNMENT = "create assignment"
        const val LOG_TYPE_CONFIRM_REDEMPTION = "Confirm redemption"
        const val LOG_TYPE_DECLINE_REDEMPTION = "Decline redemption"
    }
}