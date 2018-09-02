package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.store.reducer.ProfileAddressAddReducer

class ProfileAddressAddGetter(reducer: ProfileAddressAddReducer) : DisposableMapper() {
    val createObservable: Observable<WalletInfo>
            = reducer.createObservable
    val errorObservable: Observable<Throwable>
            = reducer.errorObservable
}