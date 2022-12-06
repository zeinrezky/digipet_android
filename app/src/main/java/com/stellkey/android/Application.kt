package com.stellkey.android

import android.app.Application
import android.os.StrictMode
import com.orhanobut.hawk.Hawk
import com.stellkey.android.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        Hawk.init(this).build()
        startKoin()
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