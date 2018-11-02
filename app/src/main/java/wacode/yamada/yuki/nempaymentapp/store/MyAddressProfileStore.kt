package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.store.creator.MyAddressProfileActionCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.MyAddressProfileGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyAddressProfileReducer
import wacode.yamada.yuki.nempaymentapp.store.type.MyAddressProfileActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyAddressProfileUseCase
import javax.inject.Inject

class MyAddressProfileStore @Inject constructor(private val useCase: MyAddressProfileUseCase) : Store<
        MyAddressProfileActionType, MyAddressProfileActionCreator, MyAddressProfileReducer, MyAddressProfileGetter>() {
    override fun createActionCreator(dispatch: (MyAddressProfileActionType) -> Unit, reducer: MyAddressProfileReducer)
            = MyAddressProfileActionCreator(useCase, dispatch)

    override fun createReducer(action: Observable<MyAddressProfileActionType>)
            = MyAddressProfileReducer(action)

    override fun createGetter(reducer: MyAddressProfileReducer)
            = MyAddressProfileGetter(reducer)
}