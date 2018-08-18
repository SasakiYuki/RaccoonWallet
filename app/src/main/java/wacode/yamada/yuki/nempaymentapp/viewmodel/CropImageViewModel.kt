package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.isseiaoki.simplecropview.CropImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CropImageViewModel @Inject constructor() : ViewModel(), LoadingStatus by LoadingStatusImpl() {
    private val compositeDisposable = CompositeDisposable()
    private val compressFormat = Bitmap.CompressFormat.JPEG
    val uriLiveData: MutableLiveData<Uri> = MutableLiveData()

    fun loadImage(cropImageView: CropImageView, uri: Uri) {
        cropImageView.load(uri).executeAsCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe {}
                .let { compositeDisposable.add(it) }
    }

    fun cropAndSaveImage(cropImageView: CropImageView, contentResolver: ContentResolver) {
        cropImageView.cropImage()
                .flatMap {
                    cropImageView.save(it)
                            .compressFormat(compressFormat)
                            .executeAsSingle(createNewUri(contentResolver))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe { uri -> uriLiveData.value = uri }
                .let { compositeDisposable.add(it) }
    }

    private fun CropImageView.cropImage() = this.crop(this.sourceUri).executeAsSingle()

    private fun createNewUri(contentResolver: ContentResolver): Uri? {
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}