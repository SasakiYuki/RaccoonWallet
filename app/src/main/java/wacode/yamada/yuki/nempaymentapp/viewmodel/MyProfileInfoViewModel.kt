package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.store.MyProfileInfoStore
import javax.inject.Inject

class MyProfileInfoViewModel @Inject constructor(private val store: MyProfileInfoStore)
    : BaseViewModel() {
    val myAddressCountLiveData: MutableLiveData<Int>
            = MutableLiveData()
    private var isEditMode = false

    init {
        store.getter.myAddressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    myAddressCountLiveData.value = it
                }.let {
            addDisposable(it)
        }
    }

    private fun countUpMyAddress() {
        store.actionCreator.countUpMyAddress()
    }

    fun onInit() {
        countUpMyAddress()
    }

    fun isEditMode() = isEditMode

    fun onEditStarted() {
        isEditMode = true
    }

    fun onEditEnded() {
        isEditMode = false
    }
}