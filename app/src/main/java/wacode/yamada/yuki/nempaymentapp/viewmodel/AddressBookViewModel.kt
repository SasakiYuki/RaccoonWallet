package wacode.yamada.yuki.nempaymentapp.viewmodel

import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(private val useCase: AddressBookUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {
}