package com.stellkey.android.view.intro.auth

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentQRLoginScannerBinding
import com.stellkey.android.helper.ScanningResultListener
import com.stellkey.android.helper.ZXingBarcodeAnalyzer
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QRLoginScannerFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentQRLoginScannerBinding
    private val viewModel by inject<LoginViewModel>()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri ->
            imageUri?.let {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, it))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                }

                Timber.d("BITMAPP->${bitmap.height}")
                val intArray = IntArray(bitmap.width * bitmap.height)
                //copy pixel data from the Bitmap into the 'intArray' array
                //copy pixel data from the Bitmap into the 'intArray' array

                val source: LuminanceSource = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
                val finalBitmap = BinaryBitmap(HybridBinarizer(source))
                val reader = MultiFormatReader()
                try {
                    val result = reader.decode(finalBitmap)
                    handleUriFromScannerQr(result.text)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_q_r_login_scanner, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { bool ->
                bool.let { loading ->
                    if (loading) {
                        showWaitingDialog()
                    } else {
                        hideWaitingDialog()
                    }
                }
            }

            allProfileSelection.observe(viewLifecycleOwner) {
                addFragment(
                    LoginChooseProfileFragment.newInstance(
                        isLoginFromQR = true,
                        allProfileModel = it
                    )
                )
            }
        }
        if (allPermissionGranted()) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            // Initialize our background executor
            cameraExecutor = Executors.newSingleThreadExecutor()

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            }, ContextCompat.getMainExecutor(requireContext()))
        } else {
            callPermissionRequest()
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        dataBinding.btnImportQr.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setOnClick() {
        dataBinding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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


    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        cameraProvider?.unbindAll()

        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(
                Size(
                    dataBinding.cameraPreview.width,
                    dataBinding.cameraPreview.height
                )
            )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                // Monitors orientation values to determine the target rotation value
                val rotation: Int = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageAnalysis.targetRotation = rotation
            }
        }
        orientationEventListener.enable()

        //switch the analyzers here, i.e. MLKitBarcodeAnalyzer, ZXingBarcodeAnalyzer
        class ScanningListener : ScanningResultListener {
            override fun onScanned(result: String) {
                requireActivity().runOnUiThread {
                    imageAnalysis.clearAnalyzer()
                    cameraProvider?.unbindAll()
                    handleUriFromScannerQr(result)
                }
            }
        }


        val analyzer = ZXingBarcodeAnalyzer(ScanningListener())

        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

        preview.setSurfaceProvider(dataBinding.cameraPreview.surfaceProvider)

        val camera =
            cameraProvider?.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            camera.cameraControl.enableTorch(false)
        }
    }

    private fun handleUriFromScannerQr(qrResult: String) {
        val qrCodeData = qrResult.toUri()
        val loginToken = qrCodeData.getQueryParameter("c")
        val authToken = qrCodeData.getQueryParameter("at")

        if (loginToken != null && authToken != null) {
            AppPreference.putMainCarerLoginToken(loginToken)
            AppPreference.putUserToken(authToken)
            AppPreference.putLoginToken(loginToken)
            viewModel.getAllProfileSelection(AppPreference.getMainCarerLoginToken())
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 299
        fun newInstance() =
            QRLoginScannerFragment()
    }
}