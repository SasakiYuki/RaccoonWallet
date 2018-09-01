package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.store.type.ProfileAddressAddActionType
class ProfileAddressAddReducer(action: Observable<ProfileAddressAddActionType>) : DisposableMapper() {
    private val mCreateSubject: PublishSubject<Unit> = PublishSubject.create()
    val createObservable: Observable<Unit>
        get() = mCreateSubject
    init {
        action.ofType(ProfileAddressAddActionType.Create::class.java)
                .subscribe({
                    mCreateSubject.onNext(Unit)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}