package com.stellkey.android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.StrictMode
import com.orhanobut.hawk.Hawk
import com.stellkey.android.di.*
import com.stellkey.android.util.DevScreenTracker
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level

class Application : Application() {

    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context =
        LocaleHelper.onAttach(super.getApplicationContext())

    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        Hawk.init(this).build()
        startKoin()
        if (BuildConfig.DEBUG) {
            DevScreenTracker.initialize(
                this,
                isTrackFragment = true,
                isFilteringLibraryFragments = true,
                isTrackerActivated = false
            )
        }
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR) else EmptyLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    CoreModule,
                    HelperModule,
                    NetworkModule,
                    RepositoryModule,
                    ViewModelModule
                )
            )
        }
    }
}