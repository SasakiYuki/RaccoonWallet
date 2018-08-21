package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(private val useCase: AddressBookUseCase) : ViewModel(), LoadingStatus by LoadingStatusImpl() {
    private val compositeDisposable = CompositeDisposable()
    val friendInfoLiveData: MutableLiveData<FriendInfo> = MutableLiveData()
    val friendIconLiveData: MutableLiveData<FriendIcon> = MutableLiveData()

    fun getFriendData(friendId: Long) {
        useCase.getFriendInfo(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    friendInfoLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { compositeDisposable.add(it) }

        useCase.getFriendIcon(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .attachLoading()
                .subscribe({
                    friendIconLiveData.value = it
                }, {
                    it.printStackTrace()
                }).let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}