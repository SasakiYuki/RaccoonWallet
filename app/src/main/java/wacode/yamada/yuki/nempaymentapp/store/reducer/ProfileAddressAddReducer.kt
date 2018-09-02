package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.type.ProfileAddressAddActionType

class ProfileAddressAddReducer(action: Observable<ProfileAddressAddActionType>) : DisposableMapper() {
    private val mCreateSubject: PublishSubject<WalletInfo> = PublishSubject.create()
    private val mErrorSubject: PublishSubject<Throwable> = PublishSubject.create()

    val createObservable: Observable<WalletInfo>
        get() = mCreateSubject
    val errorObservable: Observable<Throwable>
        get() = mErrorSubject

    init {
        action.ofType(ProfileAddressAddActionType.Create::class.java)
                .subscribe({
                    mCreateSubject.onNext(it.walletInfo)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        action.ofType(ProfileAddressAddActionType.Error::class.java)
                .subscribe({
                    mErrorSubject.onNext(it.throwable)
                }).let { disposables.add(it) }
    }
}