package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.repository.MyWalletInfoRepository
import wacode.yamada.yuki.nempaymentapp.store.creator.MyWalletInfoActionCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.MyWalletInfoGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyWalletInfoReducer
import wacode.yamada.yuki.nempaymentapp.store.type.MyWalletInfoActionType
import javax.inject.Inject

class MyWalletInfoStore @Inject constructor(private val repository: MyWalletInfoRepository) : Store<
        MyWalletInfoActionType, MyWalletInfoActionCreator, MyWalletInfoReducer, MyWalletInfoGetter>() {
    override fun createActionCreator(dispatch: (MyWalletInfoActionType) -> Unit, reducer: MyWalletInfoReducer)
            = MyWalletInfoActionCreator(repository, dispatch)

    override fun createReducer(action: Observable<MyWalletInfoActionType>)
            = MyWalletInfoReducer(action)

    override fun createGetter(reducer: MyWalletInfoReducer)
            = MyWalletInfoGetter(reducer)
}