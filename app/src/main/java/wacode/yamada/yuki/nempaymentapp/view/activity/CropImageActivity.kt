package wacode.yamada.yuki.nempaymentapp.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_crop_image.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.checkPermission

class CropImageActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun setLayout() = R.layout.activity_crop_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickUpImageForStorage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_STORAGE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                    loadImage(Utils.ensureUriPermission(this, data))
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

        saveButton.setOnClickListener {
            //todo
        }

        rotateLeftButton.setOnClickListener {
            cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
        }

        rotateRightButton.setOnClickListener {
            cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
        }
    }

    private fun pickUpImageForStorage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun loadImage(uri: Uri) {
        showProgress()
        cropImageView.load(uri).executeAsCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideProgress()
                }.let { addDispose(it) }
    }

    private fun addDispose(disposable: Disposable) {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.add(disposable)
        }
    }

    private fun unSubscribe() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private val cropMode by lazy {
        intent.getSerializableExtra(PARAM_CROP_MODE) as CropImageView.CropMode
    }

    companion object {
        private const val REQUEST_PERMISSION_STORAGE = 1000
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val PARAM_CROP_MODE = "param_crop_mode"

        fun createIntent(context: Context, cropMode: CropImageView.CropMode = CropImageView.CropMode.FIT_IMAGE): Intent {
            val intent = Intent(context, CropImageActivity::class.java)
            intent.putExtra(PARAM_CROP_MODE, cropMode)
            return intent
        }
    }
}