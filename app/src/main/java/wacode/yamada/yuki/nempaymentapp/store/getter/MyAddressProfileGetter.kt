package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyAddressProfileReducer

class MyAddressProfileGetter(reducer: MyAddressProfileReducer) : DisposableMapper() {
    val createObservable: Observable<MyAddress>
            = reducer.createObservable
}