package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class FriendInfoViewModel @Inject constructor(private val useCase: AddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val friendInfoLiveData: MutableLiveData<FriendInfo> = MutableLiveData()

    fun getFriendInfo(friendId: Long) {
        useCase.getFriendInfo(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    friendInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }
}