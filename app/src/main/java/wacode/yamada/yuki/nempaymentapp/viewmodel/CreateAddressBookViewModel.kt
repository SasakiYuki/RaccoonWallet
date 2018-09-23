package wacode.yamada.yuki.nempaymentapp.viewmodel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.CreateAddressBookUseCase
import javax.inject.Inject


class CreateAddressBookViewModel @Inject constructor(private val useCase: CreateAddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {

    fun insertFriendData(friendInfo: FriendInfo, walletList: List<WalletInfo>) {
        useCase.insertFriendInfo(friendInfo)
                .andThen(useCase.queryLatestFriendInfo())
                .flatMapCompletable { info ->
                    Observable.fromIterable(walletList)
                            .map { FriendAddress(walletInfoId = it.id, friendId = info.id) }
                            .flatMapCompletable { useCase.insertFriendWallet(it) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({}, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }
}