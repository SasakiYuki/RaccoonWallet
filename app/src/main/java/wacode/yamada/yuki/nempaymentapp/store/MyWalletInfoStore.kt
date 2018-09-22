package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.store.creator.MyWalletInfoActionCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.MyWalletInfoGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyWalletInfoReducer
import wacode.yamada.yuki.nempaymentapp.store.type.MyWalletInfoActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyWalletInfoUseCase
import javax.inject.Inject

class MyWalletInfoStore @Inject constructor(private val useCase: MyWalletInfoUseCase) : Store<
        MyWalletInfoActionType, MyWalletInfoActionCreator, MyWalletInfoReducer, MyWalletInfoGetter>() {
    override fun createActionCreator(dispatch: (MyWalletInfoActionType) -> Unit, reducer: MyWalletInfoReducer)
            = MyWalletInfoActionCreator(useCase, dispatch)

    override fun createReducer(action: Observable<MyWalletInfoActionType>)
            = MyWalletInfoReducer(action)

    override fun createGetter(reducer: MyWalletInfoReducer)
            = MyWalletInfoGetter(reducer)
}