package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import wacode.yamada.yuki.nempaymentapp.store.ProfileAddressAddStore
import javax.inject.Inject

class ProfileAddressAddViewModel @Inject constructor(private val store: ProfileAddressAddStore) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()
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
                .let { disposables.add(it) }
    }

    override fun onCleared() {
        disposables.clear()
        store.clearDisposables()
    }
}