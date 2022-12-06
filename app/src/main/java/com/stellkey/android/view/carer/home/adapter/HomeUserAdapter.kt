package com.stellkey.android.view.carer.home.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.databinding.ItemHomeUserSliderBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.stringDateToCalendar
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.IntroModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class HomeUserAdapter(context: Context, private val userList: List<AllKidsModel>) :
    RecyclerView.Adapter<HomeUserAdapter.HomeUserSliderViewHolder>() {

    private val contexts = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUserSliderViewHolder {
        return HomeUserSliderViewHolder(
            ItemHomeUserSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: HomeUserSliderViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class HomeUserSliderViewHolder(private val binding: ItemHomeUserSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: AllKidsModel) {
            /*val calendarFrom = stringDateToCalendar(
                user.activeAssignments.assignments[0].assignDate,
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            )
            val calendarTo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stringDateToCalendar(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")),
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val msDiff = calendarTo?.timeInMillis?.minus(calendarFrom?.timeInMillis ?: 0) ?: 0
            val daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)*/

            binding.apply {
                ivAvatar.loadImage(user.profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
                tvProfileName.textOrNull(user.name)

                /*if(daysDiff.toInt() > 0) {
                    ivNotificationCount.isVisible = true
                    tvNotificationCount.apply {
                        isVisible = true
                        textOrNull = daysDiff.toString()
                    }
                }
                else{
                    ivNotificationCount.isVisible = false
                    tvNotificationCount.isVisible = true
                }*/

            }
        }
    }

}