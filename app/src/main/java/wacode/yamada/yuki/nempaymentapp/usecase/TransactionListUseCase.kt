package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.TransactionRepository
import javax.inject.Inject


class TransactionListUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {
}