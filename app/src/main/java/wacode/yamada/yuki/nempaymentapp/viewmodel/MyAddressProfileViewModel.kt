package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.MyProfileEvent
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.MyAddressProfileStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class MyAddressProfileViewModel @Inject constructor(private val store: MyAddressProfileStore) : BaseViewModel() {
    val insertMyAddressLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val myProfileEntityEvent: MutableLiveData<MyProfileEntity>
            = MutableLiveData()
    val insertWalletInfoLiveData: MutableLiveData<WalletInfo>
            = MutableLiveData()

    init {
        store.getter.insertMyAddressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    insertMyAddressLiveData.value = Unit
                })
                .let {
                    addDisposable(it)
                }

        store.getter.insertWalletInfoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    insertWalletInfoLiveData.value = it
                })
                .let {
                    addDisposable(it)
                }

        RxBus.receive(MyProfileEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myProfileEntityEvent.value = it.myProfileEntity
                }
    }

    fun insertMyAddress(myAddress: MyAddress) {
        store.actionCreator.insertMyAddress(myAddress)
    }

    fun insertWalletInfo(walletInfo: WalletInfo) {
        store.actionCreator.insertWalletInfo(walletInfo)
    }
}