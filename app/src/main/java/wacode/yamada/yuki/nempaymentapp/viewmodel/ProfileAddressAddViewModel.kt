package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.store.ProfileAddressAddStore
import javax.inject.Inject

class ProfileAddressAddViewModel @Inject constructor(private val store: ProfileAddressAddStore) : BaseViewModel() {
    val createLiveData: MutableLiveData<Unit>
            = MutableLiveData()
    var isMaster = false

    init {
        store.getter.createObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    createLiveData.value = it
                })
                .let { addDisposable(it) }
    }

    override fun onCleared() {
        super.onCleared()
        store.clearDisposables()
    }
}