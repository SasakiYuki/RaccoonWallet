package wacode.yamada.yuki.nempaymentapp.view.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CropImageActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val compressFormat = Bitmap.CompressFormat.JPEG

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

        saveButton.setOnClickListener { cropAndSaveImage() }

        rotateLeftButton.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D) }

        rotateRightButton.setOnClickListener { cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D) }
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

    private fun cropAndSaveImage() {
        showProgress()
        cropImageView.cropImage()
                .flatMap {
                    cropImageView.save(it)
                            .compressFormat(compressFormat)
                            .executeAsSingle(createNewUri())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { uri ->
                    hideProgress()
                    setResult(Activity.RESULT_OK, Intent().putExtra(PARAM_INTENT_RESULT_URI, uri.toString()))
                    finish()
                }
                .let { addDispose(it) }
    }

    private fun CropImageView.cropImage() = this.crop(this.sourceUri).executeAsSingle()

    private fun createNewUri(): Uri? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val title = SimpleDateFormat("yyyyMMdd_HHmmss").format(today)
        val fileName = "image" + title + ".jpeg"
        val path = getDirPath() + "/" + fileName
        val file = File(path)
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATA, path)
        val time = currentTimeMillis / 1000
        values.put(MediaStore.MediaColumns.DATE_ADDED, time)
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time)
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length())
        }

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun getDirPath(): String {
        var dirPath = ""
        var imageDir: File? = null
        val extStorageDir = Environment.getExternalStorageDirectory()
        if (extStorageDir.canWrite()) {
            imageDir = File(extStorageDir.path + "/RaccoonWallet")
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.path
            }
        }
        return dirPath
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
        const val REQUEST_CODE_CROP_IMAGE = 1000
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