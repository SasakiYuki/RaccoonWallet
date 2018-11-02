package wacode.yamada.yuki.nempaymentapp.store.getter

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.DisposableMapper
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyProfileInfoReducer

class MyProfileInfoGetter(reducer: MyProfileInfoReducer) : DisposableMapper() {
    val myAddressObservable: Observable<Int> = reducer.myAddressObservable
    val myProfileEntityObservable: Observable<MyProfileEntity> = reducer.myProfileEntityObservable
    val updateObservable: Observable<MyProfileEntity> = reducer.updateObservable
    val errorObservable: Observable<Throwable> = reducer.errorObservable
}