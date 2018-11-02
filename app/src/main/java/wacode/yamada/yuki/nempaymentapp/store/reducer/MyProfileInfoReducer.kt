package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.store.type.MyProfileInfoActionType

class MyProfileInfoReducer(actionType: Observable<MyProfileInfoActionType>) : DisposableMapper() {
    private val mMyAddressCountSubject: PublishSubject<Int> = PublishSubject.create()
    private val mMyProfileEntitySubject: PublishSubject<MyProfileEntity> = PublishSubject.create()
    private val mUpdateSubject: PublishSubject<MyProfileEntity> = PublishSubject.create()
    private val mErrorSubject: PublishSubject<Throwable> = PublishSubject.create()

    val myAddressObservable: Observable<Int>
        get() = mMyAddressCountSubject
    val myProfileEntityObservable: Observable<MyProfileEntity>
        get() = mMyProfileEntitySubject
    val updateObservable: Observable<MyProfileEntity>
        get() = mUpdateSubject
    val errorObservable: Observable<Throwable>
        get() = mErrorSubject

    init {
        actionType.ofType(MyProfileInfoActionType.WalletInfoCount::class.java)
                .subscribe({
                    mMyAddressCountSubject.onNext(it.walletCount)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        actionType.ofType(MyProfileInfoActionType.ReceiveMyProfile::class.java)
                .subscribe({
                    mMyProfileEntitySubject.onNext(it.myProfileEntity)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        actionType.ofType(MyProfileInfoActionType.UpdateMyProfile::class.java)
                .subscribe({
                    mUpdateSubject.onNext(it.myProfileEntity)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        actionType.ofType(MyProfileInfoActionType.Error::class.java)
                .subscribe({
                    mErrorSubject.onNext(it.throwable)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}