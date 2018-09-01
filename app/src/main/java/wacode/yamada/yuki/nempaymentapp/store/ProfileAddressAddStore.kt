package wacode.yamada.yuki.nempaymentapp.store

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.flux.Store
import wacode.yamada.yuki.nempaymentapp.repository.MyProfileRepository
import wacode.yamada.yuki.nempaymentapp.store.creator.ProfileAddressAddActionCreator
import wacode.yamada.yuki.nempaymentapp.store.getter.ProfileAddressAddGetter
import wacode.yamada.yuki.nempaymentapp.store.reducer.ProfileAddressAddReducer
import wacode.yamada.yuki.nempaymentapp.store.type.ProfileAddressAddActionType
import javax.inject.Inject
class ProfileAddressAddStore @Inject constructor(private val repository: MyProfileRepository) : Store<ProfileAddressAddActionType, ProfileAddressAddActionCreator, ProfileAddressAddReducer, ProfileAddressAddGetter>() {
    override fun createActionCreator(dispatch: (ProfileAddressAddActionType) -> Unit, reducer: ProfileAddressAddReducer): ProfileAddressAddActionCreator
            = ProfileAddressAddActionCreator(repository, dispatch, reducer)
    override fun createReducer(action: Observable<ProfileAddressAddActionType>): ProfileAddressAddReducer
            = ProfileAddressAddReducer(action)
    override fun createGetter(reducer: ProfileAddressAddReducer): ProfileAddressAddGetter
            = ProfileAddressAddGetter(reducer)
}