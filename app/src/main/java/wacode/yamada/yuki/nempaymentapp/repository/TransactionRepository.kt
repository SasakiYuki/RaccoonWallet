package wacode.yamada.yuki.nempaymentapp.repository

import wacode.yamada.yuki.nempaymentapp.rest.service.TransactionService
import javax.inject.Inject


class TransactionRepository @Inject constructor(private val transactionService: TransactionService) {

    fun geAllTransaction(address: String) = transactionService.geAllTransaction(address)
}