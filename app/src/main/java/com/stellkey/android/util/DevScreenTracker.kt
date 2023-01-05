package com.stellkey.android.util

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stellkey.android.helper.extension.registerPartialActivityLifecycleCallbacks

object DevScreenTracker {

    private lateinit var app: Application
    private var lastFragmentClass: Class<out Any>? = null
    private var trackFragments: Boolean = false
    private var filteringLibFragments: Boolean = false

    private var isActivated: Boolean = false

    fun initialize(
        application: Application,
        isTrackFragment: Boolean,
        isFilteringLibraryFragments: Boolean,
        isTrackerActivated: Boolean
    ) {
        app = application
        trackFragments = isTrackFragment
        filteringLibFragments = isFilteringLibraryFragments
        isActivated = isTrackerActivated

        if (isActivated) {
            bindComponentsListeners(application)
        }
    }

    private fun bindComponentsListeners(application: Application) {
        application.registerPartialActivityLifecycleCallbacks(
            onActivityCreated = {},
            onActivityResumed = { activity ->
                listenForResumedActivities(activity)
            })
    }

    private fun listenForResumedActivities(activity: Activity) {
        sendScreenDetails(activity.javaClass, null)
        if (trackFragments)
            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentResumed(fm, f)
                        if (!isClassExcluded(f.javaClass)) {
                            sendScreenDetails(activity.javaClass, f.javaClass)
                            if (!f.isBottomDialog())
                                lastFragmentClass = f.javaClass
                        }
                    }

                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        val lastFragment = lastFragmentClass
                        // If a Dialog Fragment is destroyed, we must rollback to the previous fragment
                        if (f.isBottomDialog() && lastFragment != null)
                            sendScreenDetails(activity.javaClass, lastFragment)
                    }
                },
                true
            )
    }

    private fun sendScreenDetails(activityClass: Class<out Any>, fragmentClass: Class<out Any>?) {
        Toast.makeText(
            app.applicationContext,
            activityClass.getClassNameWithExtension() + " > " + (fragmentClass?.getClassNameWithExtension()
                ?: ""),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isClassExcluded(clazz: Class<out Any>) =
        filteringLibFragments &&
            excludedClasses.containsKey(clazz.simpleName)

    private fun Class<out Any>.getClassNameWithExtension(): String {
        return if (this.isKotlin())
            this.simpleName + ".kt"
        else this.simpleName + ".java"
    }

    private fun Fragment.isBottomDialog() = this is BottomSheetDialogFragment

    private fun Class<out Any>.isKotlin() =
        this.declaredAnnotations.any { it.annotationClass == Metadata::class }

    private val excludedClasses = arrayListOf(
        "zzd", // Google
        "NavHostFragment", // Nav Component
        "SupportRequestManagerFragment", // Glide
    ).associateBy { it }
}