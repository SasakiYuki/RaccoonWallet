package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.store.creator.MyProfileInfoActionCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.MyProfileInfoGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.MyProfileInfoReducer
import wacode.yamada.yuki.nempaymentapp.store.type.MyProfileInfoActionType
import wacode.yamada.yuki.nempaymentapp.usecase.MyProfileInfoUseCase
import javax.inject.Inject

class MyProfileInfoStore @Inject constructor(private val useCase: MyProfileInfoUseCase) : Store<
        MyProfileInfoActionType, MyProfileInfoActionCreator, MyProfileInfoReducer, MyProfileInfoGetter>() {
    override fun createActionCreator(dispatch: (MyProfileInfoActionType) -> Unit, reducer: MyProfileInfoReducer) = MyProfileInfoActionCreator(useCase, dispatch)

    override fun createReducer(action: Observable<MyProfileInfoActionType>) = MyProfileInfoReducer(action)

    override fun createGetter(reducer: MyProfileInfoReducer): MyProfileInfoGetter = MyProfileInfoGetter(reducer)
}