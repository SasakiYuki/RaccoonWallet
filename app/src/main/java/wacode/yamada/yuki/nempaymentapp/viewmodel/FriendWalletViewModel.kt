package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class FriendWalletViewModel @Inject constructor(private val useCase: AddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val walletInfoLiveData: MutableLiveData<WalletInfo> = MutableLiveData()

    fun queryWalletInfo(friendId: Long) {
        useCase.queryFriendAddress(friendId)
                .flatMapObservable { Observable.fromIterable(it) }
                .flatMapSingle { useCase.queryWalletInfo(it.walletInfoId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    walletInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }

    fun insertFriendAddress(friendId: Long, walletInfo: WalletInfo) {
        Single.just(walletInfo)
                .map { FriendAddress(friendId = friendId, walletInfoId = walletInfo.id) }
                .flatMapCompletable { useCase.insertFriendAddress(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .let { addDisposable(it) }
    }
}