package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType

class MyAddressProfileReducer(action: Observable<MyAddressProfileActionType>) : DisposableMapper() {
    private val mCreateSubject: PublishSubject<MyAddress> = PublishSubject.create()
    val createObservable: Observable<MyAddress>
        get() = mCreateSubject

    init {
        action.ofType(MyAddressProfileActionType.Create::class.java)
                .subscribe({
                    mCreateSubject.onNext(it.myAddress)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}