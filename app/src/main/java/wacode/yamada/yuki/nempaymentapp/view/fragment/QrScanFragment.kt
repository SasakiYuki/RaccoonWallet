package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.camera.CameraSettings
import kotlinx.android.synthetic.main.fragment_qr_scan.*
import kotlinx.android.synthetic.main.view_barcode_scanner.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.checkPermission
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.QrScanCallback


class QrScanFragment : BaseFragment() {
    private var isFirstVisible: Boolean = false
    private var isTouchOff: Boolean = true
    private var isUseBackCamera: Boolean = true

    private val shouldCloseThisFragment by lazy {
        arguments?.getBoolean(ARG_SHOULD_CLOSE_THIS_FRAGMENT, true) ?: true
    }

    override fun layoutRes() = R.layout.fragment_qr_scan

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        requestCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        qrScanCamera.resume()
    }

    private fun setupViews() {
        flashButton.setOnClickListener {
            isTouchOff = if (isTouchOff) {
                qrScanCamera.setTorchOn()
                false
            } else {
                qrScanCamera.setTorchOff()
                true
            }
        }

        changeCameraButton.setOnClickListener {
            qrScanCamera.pause()
            isUseBackCamera = if (isUseBackCamera) {
                setupCamera(CAMERA_FACING_FRONT)
                false
            } else {
                setupCamera(CAMERA_FACING_BACK)
                true
            }
        }
    }

    private fun setupCamera(cameraMode: Int) {
        val cameraSetting = CameraSettings()
        cameraSetting.requestedCameraId = cameraMode
        zxing_barcode_surface.cameraSettings = cameraSetting
        qrScanCamera.setStatusText("")

        activity?.let {
            if (shouldCallbackSingle) {
                qrScanCamera.decodeSingle(object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult?) {
                        if (shouldCloseThisFragment) {
                            it.supportFragmentManager.beginTransaction().remove(this@QrScanFragment).commit()
                        }
                        (this@QrScanFragment.activity as QrScanCallback).onQrScanResult(result)
                    }

                    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                    }
                })
            } else {
                qrScanCamera.decodeContinuous(object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult?) {
                        if (shouldCloseThisFragment) {
                            it.supportFragmentManager.beginTransaction().remove(this@QrScanFragment).commit()
                        }
                        (this@QrScanFragment.activity as QrScanCallback).onQrScanResult(result)
                    }

                    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                    }
                })
            }
        }

        qrScanCamera.resume()
    }

    private fun requestCameraPermission() {
        activity?.let {
            if (it.checkPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                setupCamera(CAMERA_FACING_BACK)
            } else {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), 0)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        qrScanCamera.pause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onResume()
            isFirstVisible = true
        } else {
            if (isFirstVisible) {
                qrScanCamera.pause()
            }
        }
    }

    private val shouldCallbackSingle by lazy {
        arguments?.getBoolean(ARG_SHOULD_CALLBACK_SINGLE) ?: true
    }

    companion object {
        private const val ARG_SHOULD_CLOSE_THIS_FRAGMENT = "arg_should_close_this_fragment"
        private const val ARG_SHOULD_CALLBACK_SINGLE = "arg_should_callback_single"
        const val CAMERA_FACING_BACK = 0
        const val CAMERA_FACING_FRONT = 1

        fun newInstance(shouldCloseThisFragment: Boolean, shouldCallbackSingle: Boolean = false): QrScanFragment {
            val fragment = QrScanFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.qr_scan_fragment_title)
                putBoolean(ARG_SHOULD_CLOSE_THIS_FRAGMENT, shouldCloseThisFragment)
                putBoolean(ARG_SHOULD_CALLBACK_SINGLE, shouldCallbackSingle)
            }
            fragment.arguments = args
            return fragment
        }
    }
}