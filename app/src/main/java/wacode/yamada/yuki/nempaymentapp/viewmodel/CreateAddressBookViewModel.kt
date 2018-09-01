package wacode.yamada.yuki.nempaymentapp.viewmodel

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.CreateAddressBookUseCase
import javax.inject.Inject


class CreateAddressBookViewModel @Inject constructor(private val useCase: CreateAddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {

    fun insertFriendData(iconUri: String?, friendInfo: FriendInfo) {
        useCase.insertFriendInfo(friendInfo)
                .andThen(useCase.getLatestFriendInfo())
                .flatMapCompletable { insertIconCompletable(iconUri, it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({}, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }

    private fun insertIconCompletable(iconUri: String?, friendInfo: FriendInfo): Completable {
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
}