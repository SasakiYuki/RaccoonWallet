package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.ContentResolver
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.CreateAddressBookUseCase
import javax.inject.Inject


class CreateAddressBookViewModel @Inject constructor(private val useCase: CreateAddressBookUseCase) : ViewModel(), LoadingStatus by LoadingStatusImpl() {
    private val compositeDisposable = CompositeDisposable()

    fun insertFriendData(contentResolver: ContentResolver, iconUri: String?, friendInfo: FriendInfo) {
        useCase.insertFriendInfo(friendInfo)
                .andThen(useCase.getLatestFriendInfo())
                .flatMapCompletable { insertIconCompletable(contentResolver, iconUri, it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({}, {
                    it.printStackTrace()
                }).let { compositeDisposable.add(it) }
    }

    private fun insertIconCompletable(contentResolver: ContentResolver, iconUri: String?, friendInfo: FriendInfo): Completable {
        return iconUri?.let { uri ->
            Single.create<FriendIcon> { emitter ->
                emitter.onSuccess(FriendIcon(friendInfo.id, iconUri))
            }.flatMapCompletable {
                useCase.insertFriendIcon(it)
            }
        } ?: run {
            Completable.complete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}