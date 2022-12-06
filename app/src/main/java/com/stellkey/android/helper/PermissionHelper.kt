package com.stellkey.android.helper

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionHelper {

    companion object {
        private var listener: PermissionListener? = null

        interface PermissionListener {
            fun onPermissionGranted(isAllGranted: Boolean)
        }

        fun setPermissionListener(listener: PermissionListener) {
            this.listener = listener
        }

        fun requestPermission(activity: Activity, permissions: Collection<String>) {
            Dexter.withActivity(activity).withPermissions(permissions)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                listener?.onPermissionGranted(true)
                            } else {
                                listener?.onPermissionGranted(false)
                                activity.finish()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }
    }

}