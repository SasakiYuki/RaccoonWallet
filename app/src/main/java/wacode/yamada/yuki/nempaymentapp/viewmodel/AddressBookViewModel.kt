package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(private val useCase: AddressBookUseCase) : ViewModel(), LoadingStatus by LoadingStatusImpl() {
    private val compositeDisposable = CompositeDisposable()

    fun insertFriendData(contentResolver: ContentResolver, iconUri: Uri?, friendInfo: FriendInfo) {
        useCase.insertFriendInfo(friendInfo)
                .andThen(useCase.getLatestFriendInfo())
                .flatMapCompletable { friendInfo -> insertIconCompletable(contentResolver, iconUri, friendInfo) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({}, {
                    it.printStackTrace()
                }).let { compositeDisposable.add(it) }
    }

    private fun insertIconCompletable(contentResolver: ContentResolver, iconUri: Uri?, friendInfo: FriendInfo): Completable {
        return iconUri?.let { uri ->
            Single.create<ByteArray> { emitter ->
                val icon = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                emitter.onSuccess(byteArrayOutputStream.toByteArray())
            }
                    .map { FriendIcon(friendInfo.id, it) }
                    .flatMapCompletable { useCase.insertFriendIcon(it) }
        } ?: run {
            Completable.complete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}