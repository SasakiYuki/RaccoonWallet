package wacode.yamada.yuki.nempaymentapp.view.activity

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_crop_image.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.checkPermission
import wacode.yamada.yuki.nempaymentapp.viewmodel.CropImageViewModel
import javax.inject.Inject

class CropImageActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CropImageViewModel

    private val cropMode by lazy {
        intent.getSerializableExtra(PARAM_CROP_MODE) as CropImageView.CropMode
    }

    override fun setLayout() = R.layout.activity_crop_image

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CropImageViewModel::class.java)
        setupViewModel()

        setupViews()
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickUpImageForStorage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                    viewModel.loadImage(cropImageView, Utils.ensureUriPermission(this, data))
                }
            }
            Activity.RESULT_CANCELED -> finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> pickUpImageForStorage()
                PackageManager.PERMISSION_DENIED -> finish()
            }
        }
    }

    private fun setupViews() {
        cropImageView.setCropMode(cropMode)

        closeButton.setOnClickListener { finish() }

        saveButton.setOnClickListener { viewModel.cropAndSaveImage(cropImageView, contentResolver) }

        rotateLeftButton.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D) }

        rotateRightButton.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D) }
    }

    private fun pickUpImageForStorage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun setupViewModel() {
        viewModel.run {
            loadingStatus.observe(this@CropImageActivity, Observer<Boolean> {
                it ?: return@Observer
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            })

            uriLiveData.observe(this@CropImageActivity, Observer {
                it ?: return@Observer
                setResult(Activity.RESULT_OK, Intent().putExtra(PARAM_INTENT_RESULT_URI, it.toString()))
                finish()
            })
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_STORAGE = 1001
        private const val REQUEST_CODE_PICK_IMAGE = 1002
        private const val PARAM_CROP_MODE = "param_crop_mode"
        const val PARAM_INTENT_RESULT_URI = "param_intent_result_uri"

        fun createIntent(context: Context, cropMode: CropImageView.CropMode = CropImageView.CropMode.FIT_IMAGE): Intent {
            val intent = Intent(context, CropImageActivity::class.java)
            intent.putExtra(PARAM_CROP_MODE, cropMode)
            return intent
        }
    }
}