package com.stellkey.android.view.child

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityChildMainBinding
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.util.EventBusBinder
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.child.home.ChildHomeFragment
import com.stellkey.android.view.child.log.ChildLogFragment
import com.stellkey.android.view.child.month_picker.WheelPickerFragment
import com.stellkey.android.view.child.profile.ChildProfileFragment
import com.stellkey.android.view.child.reward.ChildRewardFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChildMainAct : BaseAct() {

    private val binding by viewBinding<ActivityChildMainBinding>()
    private var previousFragment: Fragment? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(gotoScreen: NavigationChildHomeEvent) {
        when (gotoScreen.goToScreen) {
            Constant.KidMenu.REWARD -> {
                binding.clKidBtmMenu.isGone = false
                addFragment(ChildRewardFragment.newInstance())
            }

            Constant.KidMenu.HOME -> {
                binding.clKidBtmMenu.isGone = false
                addFragment(ChildHomeFragment.newInstance())
            }

            Constant.KidMenu.LOG -> {
                binding.clKidBtmMenu.isGone = false
                addFragment(ChildLogFragment.newInstance())
            }

            Constant.KidWidget.DIALOG_WHEEL_PICKER -> {
                supportFragmentManager.findFragmentById(R.id.content)?.let {
                    previousFragment = it
                }
                binding.clKidBtmMenu.isGone = true
                addFragment(WheelPickerFragment.newInstance())
            }

            Constant.KidWidget.DIALOG_WHEEL_DISMISSED -> {
                binding.clKidBtmMenu.isGone = false
                previousFragment?.let {
                    if (it is ChildLogFragment) {
                        val selectedMonth = gotoScreen.extraParams[Constant.ExtraParams.MONTH_WHEEL_PICKER]?.toInt()
                        val selectedYear = gotoScreen.extraParams[Constant.ExtraParams.YEAR_WHEEL_PICKER]?.toInt()
                        addFragment(ChildLogFragment.newInstance(selectedMonth = selectedMonth, selectedYear = selectedYear))
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_main)

        EventBusBinder.bind(this)
        setView()
        setOnClick()
    }

    private fun setView() {
        addFragment(ChildHomeFragment.newInstance())

        if (AppPreference.isUpdateLocale()) {
            addFragment(ChildProfileFragment.newInstance())
        } else
            addFragment(ChildHomeFragment.newInstance())

//        setTransparentStatusBar()
    }

    fun showMenu(isShow: Boolean) {
        binding.apply {
            clKidBtmMenu.isVisible = isShow
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
                    ivReward.setImageResource(R.drawable.ic_reward)
                    ivRewardIndicator.isVisible = true
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.REWARD))
                }
            }

            binding.ivHome -> {
                binding.apply {
                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home)
                    ivHomeIndicator.isVisible = true
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.HOME))
                }
            }

            binding.ivLog -> {
                binding.apply {
                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log)
                    ivLogIndicator.isVisible = true
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.LOG))
                }
            }
        }
    }



    override fun onDestroy() {
        AppPreference.putPetShowingOnboardingTask(true)
        super.onDestroy()
    }

    class NavigationChildHomeEvent(
        val goToScreen: String,
        val extraParams: HashMap<String, String> = hashMapOf()
    )
}