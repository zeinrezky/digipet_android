package com.stellkey.android.view.intro


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.stellkey.android.R
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.intro.intro.IntroAct
import com.stellkey.android.view.onboarding.OnBoardingAct
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenAct : BaseAct() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setView()
    }

    private fun setView() {
        val uriPath = "android.resource://com.stellkey.android/" + R.raw.video_splash
        val uri: Uri = Uri.parse(uriPath)
        vvSplash.apply {
            setVideoURI(uri)
            requestFocus()
            start()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (AppPreference.getLoginToken() != emptyString || AppPreference.getCarerToken() != emptyString) {
                if (AppPreference.isCompleteLogin()) {
                    if(AppPreference.isFirstTime()){
                        startActivity(Intent(this, OnBoardingAct::class.java))
                        finish()
                    }
                    else{
                        if(AppPreference.isKidLogin()){
                            startActivity(Intent(this, ChildMainAct::class.java))
                            finish()
                        }
                        else{
                            startActivity(Intent(this, HomeAct::class.java))
                            finish()
                        }
                    }

                } else {
                    val intent = Intent(this, IntroAct::class.java)
                    intent.putExtra("isLoginDirected", true)
                    startActivity(intent)
                    finish()
                }
            } else {
                startActivity(Intent(this, IntroAct::class.java))
                finish()
            }

        }, 3000)

    }
}