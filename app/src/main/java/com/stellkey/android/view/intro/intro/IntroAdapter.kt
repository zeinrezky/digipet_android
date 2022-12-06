package com.stellkey.android.view.intro.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.databinding.ItemIntroLayoutBinding
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.IntroModel

class IntroAdapter(context: Context, private val introList: List<IntroModel>) :
    RecyclerView.Adapter<IntroAdapter.IntroSlideViewHolder>() {

    //for adding text to speech listener in the onboarding fragment
    var onTextPassed: ((textView: TextView) -> Unit)? = null

    private val contexts = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            ItemIntroLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return introList.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introList[position])
    }

    inner class IntroSlideViewHolder(private val binding: ItemIntroLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(intro: IntroModel) {
            //UtilityHelper.setImage(contexts, intro.imageUrl, binding.ivIntroImage)
            binding.ivIntroImage.setImageResource(intro.imageUrl)
            binding.tvIntroTitle.textOrNull(intro.title)
            onTextPassed?.invoke(binding.tvIntroTitle)
        }
    }

}