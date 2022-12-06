package com.stellkey.android.view.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityChildMainBinding
import com.stellkey.android.helper.extension.setTransparentStatusBar
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.child.home.ChildHomeFragment
import com.stellkey.android.view.child.reward.ChildRewardFragment

class ChildMainAct : BaseAct() {
    private val binding by viewBinding<ActivityChildMainBinding>()

    private var selectedMenu: String = Constant.KidMenu.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_main)

        setView()
        setOnClick()
    }

    private fun setView() {
        setTransparentStatusBar()

        when (selectedMenu) {
            Constant.KidMenu.REWARD -> {
                addFragment(ChildRewardFragment.newInstance())
            }
            Constant.KidMenu.HOME -> {
                addFragment(ChildHomeFragment.newInstance())
            }
            Constant.KidMenu.LOG -> {
                addFragment(ChildHomeFragment.newInstance())
            }
        }
    }

    fun showMenu(isShow: Boolean) {
        binding.apply {
            clKidBtmMenu.isVisible = isShow
            viewKidDividerBottom.isVisible = isShow
        }
    }

    private fun setOnClick() {
        binding.apply {
            ivReward.setOnClickListener(onClickCallback)
            ivHome.setOnClickListener(onClickCallback)
            ivLog.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivReward -> {
                binding.apply {
                    selectedMenu = Constant.KidMenu.REWARD
                    supportFragmentManager.popBackStack()
                    addFragment(ChildRewardFragment.newInstance())

                    ivReward.setImageResource(R.drawable.ic_reward)
                    ivRewardIndicator.isVisible = true
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                }
            }
            binding.ivHome -> {
                binding.apply {
                    selectedMenu = Constant.KidMenu.HOME
                    supportFragmentManager.popBackStack()
                    addFragment(ChildHomeFragment.newInstance())

                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home)
                    ivHomeIndicator.isVisible = true
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                }
            }
            binding.ivLog -> {
                binding.apply {
                    selectedMenu = Constant.KidMenu.LOG
                    supportFragmentManager.popBackStack()
                    addFragment(ChildHomeFragment.newInstance())

                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log)
                    ivLogIndicator.isVisible = true
                }
            }
        }
    }
}