package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType

class MyAddressProfileReducer(action: Observable<MyAddressProfileActionType>) : DisposableMapper() {
    private val mInsertMyAddressSubject: PublishSubject<Unit> = PublishSubject.create()
    private val mInsertWalletInfoSubject: PublishSubject<WalletInfo> = PublishSubject.create()

    val insertMyAddressObservable: Observable<Unit>
        get() = mInsertMyAddressSubject
    val insertWalletInfoObservable: Observable<WalletInfo>
        get() = mInsertWalletInfoSubject

    init {
        action.ofType(MyAddressProfileActionType.InsertMyAddress::class.java)
                .subscribe({
                    mInsertMyAddressSubject.onNext(Unit)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
        action.ofType(MyAddressProfileActionType.InsertWalletInfo::class.java)
                .subscribe({
                    mInsertWalletInfoSubject.onNext(it.walletInfo)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}