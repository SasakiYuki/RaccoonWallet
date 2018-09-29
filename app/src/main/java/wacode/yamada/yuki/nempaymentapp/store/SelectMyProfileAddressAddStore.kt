package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.store.creator.SelectMyProfileAddressAddCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.SelectMyProfileAddressAddGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.SelectMyProfileAddressAddReducer
import wacode.yamada.yuki.nempaymentapp.store.type.SelectMyProfileAddressAddActionType
import wacode.yamada.yuki.nempaymentapp.usecase.SelectMyProfileAddressAddUseCase
import javax.inject.Inject

class SelectMyProfileAddressAddStore @Inject constructor(private val useCase: SelectMyProfileAddressAddUseCase) : Store<
        SelectMyProfileAddressAddActionType, SelectMyProfileAddressAddCreator, SelectMyProfileAddressAddReducer, SelectMyProfileAddressAddGetter>() {
    override fun createActionCreator(dispatch: (SelectMyProfileAddressAddActionType) -> Unit, reducer: SelectMyProfileAddressAddReducer)
            = SelectMyProfileAddressAddCreator(useCase, dispatch)

    override fun createReducer(action: Observable<SelectMyProfileAddressAddActionType>)
            = SelectMyProfileAddressAddReducer(action)

    override fun createGetter(reducer: SelectMyProfileAddressAddReducer)
            = SelectMyProfileAddressAddGetter(reducer)
}