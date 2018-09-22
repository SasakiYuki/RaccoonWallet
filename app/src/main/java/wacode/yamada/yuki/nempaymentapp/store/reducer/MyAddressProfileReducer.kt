package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType

class MyAddressProfileReducer(action: Observable<MyAddressProfileActionType>) : DisposableMapper() {
    private val mCreateSubject: PublishSubject<Unit> = PublishSubject.create()
    val createObservable: Observable<Unit>
        get() = mCreateSubject

    init {
        action.ofType(MyAddressProfileActionType.Create::class.java)
                .subscribe({
                    mCreateSubject.onNext(Unit)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}