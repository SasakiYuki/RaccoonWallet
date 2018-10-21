package wacode.yamada.yuki.nempaymentapp.viewmodel

import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatus
import wacode.yamada.yuki.nempaymentapp.extentions.LoadingStatusImpl
import wacode.yamada.yuki.nempaymentapp.usecase.TransactionListUseCase
import javax.inject.Inject


class TransactionListViewModel @Inject constructor(private val useCase: TransactionListUseCase) : BaseViewModel(), LoadingStatus by LoadingStatusImpl() {

}