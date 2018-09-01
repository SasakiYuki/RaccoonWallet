package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(private val useCase: AddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
    val friendIconLiveData: MutableLiveData<FriendIcon> = MutableLiveData()

    fun getFriendData(friendId: Long) {
        useCase.getFriendIcon(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    friendIconLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { addDisposable(it) }
    }
}