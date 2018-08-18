package wacode.yamada.yuki.nempaymentapp.viewmodel

import android.arch.lifecycle.ViewModel
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.usecase.AddressBookUseCase
import javax.inject.Inject


class AddressBookViewModel @Inject constructor(useCase: AddressBookUseCase) : ViewModel(), LoadingStatus by LoadingStatusImpl() {

}