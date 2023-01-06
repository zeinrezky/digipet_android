package com.stellkey.android.view.carer.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityHomeBinding
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.carer.account.AccountFragment
import com.stellkey.android.view.carer.account.SettingFragment
import com.stellkey.android.view.carer.family.FamilyFragment
import com.stellkey.android.view.carer.log.LogFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeAct : BaseAct() {
    private val binding by viewBinding<ActivityHomeBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setView()
        setOnClick()
    }

    private fun setView() {
        if (AppPreference.isUpdateLocale()) {
            AppPreference.putUpdateLocale(false)
            addFragment(SettingFragment.newInstance())
        } else
            addFragment(HomeFragment.newInstance())

        binding.apply {
            window.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(this@HomeAct, R.color.white)
            }

            val radius = resources.getDimension(R.dimen.space_x6_half)
            val bottomNavigationViewBackground =
                bottomNav.background as MaterialShapeDrawable
            bottomNavigationViewBackground.shapeAppearanceModel =
                bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, radius)
                    .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                    .build()

            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        addFragment(HomeFragment.newInstance())
                        true
                    }
                    R.id.family -> {
                        addFragment(FamilyFragment.newInstance())
                        true
                    }
                    R.id.log -> {
                        addFragment(LogFragment.newInstance())
                        true
                    }
                    R.id.account -> {
                        addFragment(AccountFragment.newInstance())
                        true
                    }
                    else -> {
                        addFragment(HomeFragment.newInstance())
                        true
                    }
                }
            }
        }
    }

    fun showMenu(isShow: Boolean) {
        binding.apply {
            bottomNav.isVisible = isShow
            viewDividerBottom.isVisible = isShow
        }
    }

    private fun setOnClick() {
        binding.apply {

        }
    }

    fun setBadge(count: Int) {
        if (count == 0) {
            bottomNav.removeBadge(R.id.home)
        } else {
            val badge = bottomNav.getOrCreateBadge(R.id.home) // previously showBadge
            badge.apply {
                number = count
                backgroundColor = ContextCompat.getColor(this@HomeAct, R.color.red)
                badgeTextColor = ContextCompat.getColor(this@HomeAct, R.color.red)
            }
        }
    }
}