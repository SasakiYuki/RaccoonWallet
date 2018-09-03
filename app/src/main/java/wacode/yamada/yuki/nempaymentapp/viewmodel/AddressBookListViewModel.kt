package wacode.yamada.yuki.nempaymentapp.viewmodel

import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookListUseCase
import javax.inject.Inject


class AddressBookListViewModel @Inject constructor(private val useCase: AddressBookListUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
}