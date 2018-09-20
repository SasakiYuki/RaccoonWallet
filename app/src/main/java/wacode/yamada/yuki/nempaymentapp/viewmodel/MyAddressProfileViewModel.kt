package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.event.CreatedMyProfileEvent
import wacode.yamada.yuki.nempaymentapp.event.MyProfileEvent
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import wacode.yamada.yuki.nempaymentapp.store.MyAddressProfileStore
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import javax.inject.Inject

class MyAddressProfileViewModel @Inject constructor(private val store: MyAddressProfileStore) : BaseViewModel() {
    val createLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val createdMyProfileLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    val myProfileEvent: MutableLiveData<MyProfile>
            = MutableLiveData()

    init {
        store.getter.createObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    createLiveData.value = Unit
                })
                .let {
                    addDisposable(it)
                }

        RxBus.receive(CreatedMyProfileEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    createdMyProfileLiveData.value = Unit
                }.let { addDisposable(it) }

        RxBus.receive(MyProfileEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myProfileEvent.value = it.myProfile
                }

    }

    fun create(myAddress: MyAddress) {
        store.actionCreator.create(myAddress)
    }
}