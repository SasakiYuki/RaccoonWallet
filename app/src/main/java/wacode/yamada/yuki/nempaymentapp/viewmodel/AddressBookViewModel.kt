package wacode.yamada.yuki.nempaymentapp.viewmodel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(private val useCase: AddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {

    fun updateFriendInfo(friendInfo: FriendInfo) {
        useCase.updateFriendInfo(friendInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
                .let { addDisposable(it) }
    }
}