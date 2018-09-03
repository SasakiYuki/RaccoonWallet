package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyWalletInfoReducer

class MyWalletInfoGetter(reducer: MyWalletInfoReducer) :DisposableMapper(){
    val walletInfoObservable: Observable<WalletInfo>
            = reducer.walletInfoObservable
    val myAddressObservable: Observable<MyAddress>
            = reducer.myAddressObservable
    val errorObservable: Observable<Throwable>
            = reducer.errorObservable
}