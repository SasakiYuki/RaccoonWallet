package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.store.reducer.SelectMyProfileAddressAddReducer

class SelectMyProfileAddressAddGetter(reducer: SelectMyProfileAddressAddReducer) : DisposableMapper() {
    val allWalletObservable: Observable<List<Wallet>>
            = reducer.allWalletObservable
    val myAddressObservable: Observable<MyAddress>
            = reducer.myAddressObservable
    val walleInfoObservable: Observable<WalletInfo>
            = reducer.walleInfoObservable
}