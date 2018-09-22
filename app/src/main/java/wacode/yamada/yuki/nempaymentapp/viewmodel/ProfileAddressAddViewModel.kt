package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.WalletInfoEvent
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.ProfileAddressAddStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class ProfileAddressAddViewModel @Inject constructor(private val store: ProfileAddressAddStore) : BaseViewModel() {
    val createLiveData: MutableLiveData<WalletInfo>
            = MutableLiveData()
    val errorLiveData: MutableLiveData<Throwable>
            = MutableLiveData()
    var isMaster = false

    init {
        store.getter.createObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    createLiveData.value = it
                    RxBus.send(WalletInfoEvent(it))
                })
                .let { addDisposable(it) }
        store.getter.errorObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    errorLiveData.value = it
                })
    }

    fun create(walletInfo: WalletInfo) {
        store.actionCreator.create(walletInfo)
    }

    override fun onCleared() {
        super.onCleared()
        store.clearDisposables()
    }
}