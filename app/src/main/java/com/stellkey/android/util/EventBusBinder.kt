package com.stellkey.android.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.BuildConfig
import com.stellkey.android.helper.extension.safeLeakRun
import java.util.concurrent.atomic.AtomicReference
import org.greenrobot.eventbus.EventBus

object EventBusBinder : EventBus() {
    /**
     * EventBus tool:
     * bind or register immediately
     * and unregister when [androidx.lifecycle.Lifecycle.Event.ON_DESTROY]
     * from  given activity.
     *
     * Will call default method:
     *
     *  1. [EventBus.register]
     *  1. [EventBus.unregister]
     *
     *
     * @param activity
     */
    @JvmStatic
    fun bind(activity: AppCompatActivity) {
        if (getDefault().isRegistered(activity)) return
        val observer = AtomicReference<LifecycleEventObserver>()
        observer.set(LifecycleEventObserver { source: LifecycleOwner?, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                getDefault().unregister(activity)
                activity.lifecycle.removeObserver(observer.get())
            }
        })
        getDefault().register(activity)
        activity.lifecycle.addObserver(observer.get())
    }

    @JvmStatic
    fun bind(fragment: Fragment) {
        if (getDefault().isRegistered(fragment)) return
        val observer = AtomicReference<LifecycleEventObserver>()
        observer.set(LifecycleEventObserver { source: LifecycleOwner?, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                getDefault().unregister(fragment)
                fragment.lifecycle.removeObserver(observer.get())
            }
        })
        getDefault().register(fragment)
        fragment.lifecycle.addObserver(observer.get())
    }

    @JvmStatic
    fun bind(viewHolder: RecyclerView.ViewHolder) {

        viewHolder.itemView.safeLeakRun({
            if (getDefault().isRegistered(viewHolder).not())
                getDefault().register(viewHolder)
        }, {
            getDefault().unregister(viewHolder)
        })
    }

    @JvmStatic
    fun init(){
        builder()
            .throwSubscriberException(BuildConfig.DEBUG)
            .logNoSubscriberMessages(BuildConfig.DEBUG)
            .logSubscriberExceptions(BuildConfig.DEBUG)
            .sendNoSubscriberEvent(false)
            .installDefaultEventBus()

    }
}