package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.BottomCompleteButtonEvent
import wacode.yamada.yuki.nempaymentapp.event.BottomEditButtonEvent
import wacode.yamada.yuki.nempaymentapp.event.MasterWalletInfoEvent
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.store.MyProfileInfoStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class MyProfileInfoViewModel @Inject constructor(private val store: MyProfileInfoStore)
    : BaseViewModel() {
    val myAddressCountLiveData: MutableLiveData<Int>
            = MutableLiveData()
    val myProfileEntityLiveData: MutableLiveData<MyProfileEntity>
            = MutableLiveData()
    val addressLiveData: MutableLiveData<String>
            = MutableLiveData()
    val createEventLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val updateEventLiveData: MutableLiveData<MyProfileEntity>
            = MutableLiveData()
    val bottomEditButtonEventLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val bottomCompleteButtonEventLiveData: MutableLiveData<Unit>
            = MutableLiveData()

    init {
        store.getter.myAddressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myAddressCountLiveData.value = it
                }.let {
            addDisposable(it)
        }
        store.getter.myProfileEntityObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myProfileEntityLiveData.value = it
                }.let {
            addDisposable(it)
        }
        store.getter.updateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateEventLiveData.value = it
                }.let {
            addDisposable(it)
        }
        RxBus.receive(BottomEditButtonEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    bottomEditButtonEventLiveData.value = Unit
                }.let { addDisposable(it) }
        RxBus.receive(BottomCompleteButtonEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    bottomCompleteButtonEventLiveData.value = Unit
                }.let { addDisposable(it) }
        RxBus.receive(MasterWalletInfoEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressLiveData.value = it.walletInfo.walletAddress
                }
    }

    private fun countUpMyAddress() {
        store.actionCreator.countUpMyAddress()
    }

    private fun loadMyProfile() {
        store.actionCreator.loadMyProfile()
    }

    fun onInit() {
        countUpMyAddress()
        loadMyProfile()
    }

    fun update(myProfileEntity: MyProfileEntity) {
        store.actionCreator.updateMyProfile(myProfileEntity)
    }
}