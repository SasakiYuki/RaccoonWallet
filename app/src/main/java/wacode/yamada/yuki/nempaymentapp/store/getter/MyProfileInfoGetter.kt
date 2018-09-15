package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyProfileInfoReducer

class MyProfileInfoGetter(reducer: MyProfileInfoReducer) : DisposableMapper() {
    val myAddressObservable: Observable<Int> = reducer.myAddressObservable
    val myProfileObservable: Observable<MyProfile> = reducer.myProfileObservable
    val errorObservable: Observable<Throwable> = reducer.errorObservable
}