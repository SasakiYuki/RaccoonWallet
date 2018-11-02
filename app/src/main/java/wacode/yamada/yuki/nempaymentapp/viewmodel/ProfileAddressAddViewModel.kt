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
    val insertLiveData: MutableLiveData<WalletInfo>
            = MutableLiveData()
    val updateLiveData: MutableLiveData<WalletInfo>
            = MutableLiveData()
    val errorLiveData: MutableLiveData<Throwable>
            = MutableLiveData()
    var isMaster = false

    init {
        store.getter.insertObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    insertLiveData.value = it
                    RxBus.send(WalletInfoEvent.InsertWalletInfo(it))
                })
                .let { addDisposable(it) }
        store.getter.updateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateLiveData.value = it
                    RxBus.send(WalletInfoEvent.UpdateWalletInfo(it))
                })
        store.getter.errorObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    errorLiveData.value = it
                })
    }

    fun insert(walletInfo: WalletInfo) {
        store.actionCreator.insert(walletInfo)
    }

    fun update(walletInfo: WalletInfo) {
        store.actionCreator.update(walletInfo)
    }

    override fun onCleared() {
        super.onCleared()
        store.clearDisposables()
    }
}