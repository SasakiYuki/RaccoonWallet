package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.WalletInfoEvent
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.MyWalletInfoStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class MyWalletInfoViewModel @Inject constructor(private val store: MyWalletInfoStore) : BaseViewModel() {
    val myAddressLiveData: MutableLiveData<MyAddress>
            = MutableLiveData()
    val walletInfoLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val walletInfoItems: ArrayList<WalletInfo> = ArrayList()

    init {
        store.getter.myAddressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    myAddressLiveData.value = it
                })
                .let {
                    addDisposable(it)
                }
        store.getter.walletInfoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    walletInfoItems.add(it)
                    walletInfoLiveData.value = Unit
                })
                .let {
                    addDisposable(it)
                }
        RxBus.receive(WalletInfoEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    walletInfoItems.add(it.walletInfo)
                    walletInfoLiveData.value = Unit
                }.let { addDisposable(it) }
    }

    fun findAllMyAddress() {
        store.actionCreator.findAllMyAddress()
    }

    fun selectWalletInfo(id: Long) {
        store.actionCreator.selectWalletInfo(id)
    }
}