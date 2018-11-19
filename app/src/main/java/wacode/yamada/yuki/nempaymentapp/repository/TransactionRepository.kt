package wacode.yamada.yuki.nempaymentapp.repository

import wacode.yamada.yuki.nempaymentapp.rest.service.TransactionService
import javax.inject.Inject


class TransactionRepository @Inject constructor(private val transactionService: TransactionService) {

    fun getAllTransaction(address: String, id: Int = -1) = transactionService.geAllTransaction(address, id)

    fun getUnconfirmedTransactions(address: String) = transactionService.getUnconfirmedTransactions(address)
}