package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.type.MyWalletInfoActionType

class MyWalletInfoReducer(actionType: Observable<MyWalletInfoActionType>) : DisposableMapper() {
    private val mWalletInfoSubject: PublishSubject<WalletInfo> = PublishSubject.create()
    private val mMyAddressSubject: PublishSubject<MyAddress> = PublishSubject.create()
    private val mErrorSubject: PublishSubject<Throwable> = PublishSubject.create()

    val walletInfoObservable: Observable<WalletInfo>
        get() = mWalletInfoSubject
    val myAddressObservable: Observable<MyAddress>
        get() = mMyAddressSubject
    val errorObservable: Observable<Throwable>
        get() = mErrorSubject

    init {
        actionType.ofType(MyWalletInfoActionType.SelectWalletInfo::class.java)
                .subscribe({
                    mWalletInfoSubject.onNext(it.walletInfo)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        actionType.ofType(MyWalletInfoActionType.ReceiveMyAddress::class.java)
                .subscribe({
                    mMyAddressSubject.onNext(it.myAddress)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }

        actionType.ofType(MyWalletInfoActionType.Error::class.java)
                .subscribe({
                    mErrorSubject.onNext(it.throwable)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}