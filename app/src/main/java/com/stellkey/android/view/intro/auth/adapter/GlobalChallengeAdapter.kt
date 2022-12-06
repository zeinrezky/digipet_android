package com.stellkey.android.view.intro.auth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.stellkey.android.R
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidGlobalChallengeModel

class GlobalChallengeAdapter(
    context: Context,
    private val challengeList: ArrayList<KidGlobalChallengeModel>,
    private val listener: Listener
) : RecyclerView.Adapter<GlobalChallengeAdapter.GlobalChallengeViewHolder>() {

    private val contexts = context

    interface Listener {
        fun onItemClicked(data: KidGlobalChallengeModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalChallengeViewHolder {
        val view =
            LayoutInflater.from(contexts).inflate(R.layout.item_kid_challenge, parent, false)
        return GlobalChallengeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    private fun updateSelectedItem(id: Int, pos: Int) {
        challengeList.forEach {
            if (it.isSelected)
                if (it.id != id)
                    it.isSelected = !it.isSelected
        }
        challengeList[pos].isSelected = !challengeList[pos].isSelected

        //notifyItemChanged(pos)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GlobalChallengeViewHolder, position: Int) {
        holder.cvKidChallenge.apply {
            strokeColor = if (challengeList[position].isSelected) {
                setCardBackgroundColor(ContextCompat.getColor(contexts, R.color.light_blue))
                ContextCompat.getColor(contexts, R.color.green)
            } else {
                setCardBackgroundColor(ContextCompat.getColor(contexts, R.color.white))
                ContextCompat.getColor(contexts, R.color.primary_disabled)
            }
        }

        holder.ivChallenge.loadImage(
            challengeList[position].icon,
            ImageCornerOptions.ROUNDED,
            20
        )
        holder.tvChallengeName.textOrNull = challengeList[position].title

        holder.itemView.setOnClickListener {
            updateSelectedItem(challengeList[position].id, position)
            listener.onItemClicked(challengeList[position])
        }
    }

    inner class GlobalChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvKidChallenge: MaterialCardView = itemView.findViewById(R.id.cvKidChallenge)
        val ivChallenge: ImageView = itemView.findViewById(R.id.ivChallenge)
        val tvChallengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
    }
}