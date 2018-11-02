package wacode.yamada.yuki.nempaymentapp.store.reducer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.store.type.SelectMyProfileAddressAddActionType

class SelectMyProfileAddressAddReducer(action: Observable<SelectMyProfileAddressAddActionType>) : DisposableMapper() {
    private val mAllWalletSubject: PublishSubject<List<Wallet>> = PublishSubject.create()
    private val mMyAddressSubject: PublishSubject<MyAddress> = PublishSubject.create()
    private val mWalletInfoSubject: PublishSubject<WalletInfo> = PublishSubject.create()

    val allWalletObservable: Observable<List<Wallet>>
        get() = mAllWalletSubject
    val myAddressObservable: Observable<MyAddress>
        get() = mMyAddressSubject
    val walletInfoObservable: Observable<WalletInfo>
        get() = mWalletInfoSubject

    init {
        action.ofType(SelectMyProfileAddressAddActionType.ReceiveAllWallet::class.java)
                .subscribe({
                    mAllWalletSubject.onNext(it.list)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
        action.ofType(SelectMyProfileAddressAddActionType.ReceiveMyAddress::class.java)
                .subscribe({
                    mMyAddressSubject.onNext(it.myAddress)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
        action.ofType(SelectMyProfileAddressAddActionType.ReceiveWalletInfo::class.java)
                .subscribe({
                    mWalletInfoSubject.onNext(it.walletInfo)
                }, {
                    it.printStackTrace()
                }).let { disposables.add(it) }
    }
}