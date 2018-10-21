package wacode.yamada.yuki.nempaymentapp.usecase

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.repository.AccountRepository
import wacode.yamada.yuki.nempaymentapp.repository.TransactionRepository
import javax.inject.Inject


class TransactionListUseCase @Inject constructor(private val transactionRepository: TransactionRepository, private val accountRepository: AccountRepository) {
    fun getAllTransaction(address: String) = transactionRepository.getAllTransaction(address = address)
            .flatMapObservable { Observable.fromIterable(it) }

    fun getAllTransactionFromId(address: String, id: Int) = transactionRepository.getAllTransaction(address = address, id = id)

    fun getUnconfirmedTransactions(address: String) = transactionRepository.getUnconfirmedTransactions(address)
            .flatMapObservable { Observable.fromIterable(it) }

    fun getAccountAddressFromPublicKey(publicKey: String) = accountRepository.getAccountInfoFromPublicKey(publicKey)
            .map {
                it.account.address
            }
            .toObservable()
}