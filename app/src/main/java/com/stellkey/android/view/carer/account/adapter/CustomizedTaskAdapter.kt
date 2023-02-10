package com.stellkey.android.view.carer.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stellkey.android.databinding.ItemCustomisedTaskBinding
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.CustomChallengeModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.home.HomeAct
import com.zeugmasolutions.localehelper.Locales

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version CustomizedTaskAdapter, v 0.1 09/01/23 16.02 by Nicolas Manurung
 */
class CustomizedTaskAdapter(
    private val listCustomizedTask: List<CustomChallengeModel>,
    private val listener: Listener
) : Adapter<CustomizedTaskAdapter.CustomizedTaskViewHolder>() {

    interface Listener {

        fun onChallengeClicked(challenge: CustomChallengeModel)
    }

    inner class CustomizedTaskViewHolder(private val binding: ItemCustomisedTaskBinding) :
        ViewHolder(binding.root) {

        fun bind(item: CustomChallengeModel) {
            val languageLocal = AppPreference.getCarerLocale() == "en"
            binding.ivChallenge.loadImage(item.challengeCat.icon)
            binding.tvChallengeCategory.text =
                if (languageLocal) item.challengeCat.title else item.challengeCat.titleFr
            binding.tvChallengeName.text = item.title
            itemView.setOnClickListener {
                listener.onChallengeClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomizedTaskViewHolder =
        CustomizedTaskViewHolder(
            ItemCustomisedTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = listCustomizedTask.size

    override fun onBindViewHolder(holder: CustomizedTaskViewHolder, position: Int) {
        holder.bind(listCustomizedTask[position])
    }
}