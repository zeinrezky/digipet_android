package com.stellkey.android.view.carer.family.qrlogin

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentQrLoginFamilyBinding
import com.stellkey.android.helper.extension.bitmap
import com.stellkey.android.helper.extension.copyToClipboard
import com.stellkey.android.helper.extension.saveBitmap
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeViewModel
import io.github.g0dkar.qrcode.QRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.IOException


class QrLoginFamilyFragment : BaseFragment() {


    private lateinit var dataBinding: FragmentQrLoginFamilyBinding

    private val viewModel by inject<HomeViewModel>()

    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val REQUEST_CODE_PERMISSIONS = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_qr_login_family, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageOut = ByteArrayOutputStream()
        val loginToken = AppPreference.getLoginToken()
        val emailUser = AppPreference.getTempEmail()
        val deeplinkUrl = "stellkey://carerlogin/login?u=$emailUser&c=$loginToken"

        QRCode(deeplinkUrl)
            .render()
            .writeImage(imageOut)

        val bitmap = BitmapFactory.decodeByteArray(imageOut.toByteArray(), 0, imageOut.size())
        dataBinding.ivQrOutput.setImageBitmap(bitmap)
        dataBinding.tvDeeplinkUrl.text = deeplinkUrl
        dataBinding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        dataBinding.btnCopyToClip.setOnClickListener {
            requireContext().copyToClipboard(deeplinkUrl)
            Toast.makeText(requireContext(), "Success Copy", Toast.LENGTH_LONG).show()
        }
        dataBinding.btnSave.setOnClickListener {
            if (bitmap != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (allPermissionGranted()) {
                            requireContext().saveBitmap(
                                "StellKeyQRLogin",
                                dataBinding.ivQrOutput.bitmap
                            )
                            // show success message to user
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Success To Save",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        } else {
                            callPermissionRequest()
                        }
                    } catch (e: IOException) {
                        // show error message to user
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Failed To Save",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }
                }
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun callPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    companion object {
        fun newInstance() =
            QrLoginFamilyFragment()
    }
}