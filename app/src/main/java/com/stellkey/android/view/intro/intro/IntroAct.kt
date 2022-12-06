package com.stellkey.android.view.intro.intro

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityIntroBinding
import com.stellkey.android.helper.extension.autoScroll
import com.stellkey.android.helper.extension.orEmpty
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.model.IntroModel
import com.stellkey.android.view.intro.auth.LoginFragment
import com.stellkey.android.view.intro.auth.RegisterEmailFragment
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import kotlinx.android.synthetic.main.activity_intro.*

class IntroAct : BaseAct() {

    private val binding by viewBinding<ActivityIntroBinding>()

    lateinit var introSliderAdapter: IntroAdapter
    lateinit var introList: MutableList<IntroModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setView()
    }

    private fun setView() {
        val bundle: Bundle? = intent.extras
        val isLoginDirected = bundle?.getBoolean("isLoginDirected").orEmpty

        if (isLoginDirected) {
            addFragment(LoginChooseProfileFragment.newInstance())
        }
        introList = mutableListOf()
        val intro1 = IntroModel(
            "Join StellKey to help your kids develop good habits",
            R.drawable.img_intro_1,
            0
        )
        val intro2 =
            IntroModel(
                "Our fun system helps kids develop independence",
                R.drawable.img_intro_1,
                1
            )
        val intro3 =
            IntroModel("The result? More peaceful parenting", R.drawable.img_intro_2, 2)
        introList.add(intro1)
        introList.add(intro2)
        introList.add(intro3)

        binding.apply {
            introSliderAdapter = IntroAdapter(this@IntroAct, introList)
            viewPager.apply {
                adapter = introSliderAdapter
                autoScroll(
                    lifecycleScope = lifecycleScope,
                    interval = 4000L
                )
            }
            indicator.attachTo(viewPager)
        }

        //setViewPager()

        btnRegister.setOnClickListener {
            addFragment(RegisterEmailFragment.newInstance())
        }

        btnLogin.setOnClickListener {
            addFragment(LoginFragment.newInstance())
        }


    }

}