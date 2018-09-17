package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.BottomCompleteButtonEvent
import wacode.yamada.yuki.nempaymentapp.event.BottomEditButtonEvent
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import wacode.yamada.yuki.nempaymentapp.store.MyProfileInfoStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class MyProfileInfoViewModel @Inject constructor(private val store: MyProfileInfoStore)
    : BaseViewModel() {
    val myAddressCountLiveData: MutableLiveData<Int>
            = MutableLiveData()
    val myProfileLiveData: MutableLiveData<MyProfile>
            = MutableLiveData()
    val createEventLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val updateEventLiveData: MutableLiveData<Unit>
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
        store.getter.myProfileObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myProfileLiveData.value = it
                }.let {
            addDisposable(it)
        }
        store.getter.updateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateEventLiveData.value = Unit
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

    fun update(myProfile: MyProfile) {
        store.actionCreator.updateMyProfile(myProfile)
    }
}